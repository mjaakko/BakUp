package xyz.malkki.bakup

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipParameters
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.streams.toList

class BackupManager(private val directory: Path, private val outputDirectory: Path, private val maxBackups: Int) {
    companion object {
        //Add comment to archives to avoid deleting other files that could be in the output directory (do not change this value)
        private const val ARCHIVE_COMMENT = "bakup"
    }

    private fun removeOldestArchives() {
        val backUpArchiveFiles = Files.list(outputDirectory)
            .sorted(FileLastModifiedComparator.reversed())
            .filter { path -> isBackupArchive(path) }
            .toList()

        println("${backUpArchiveFiles.size} backups found")
        val newest = backUpArchiveFiles.take(maxBackups - 1).toHashSet()
        backUpArchiveFiles.forEach { path ->
            //Remove oldest archives
            if (!newest.contains(path)) {
                println("Deleting ${path.fileName}")
                Files.delete(path)
            }
        }
    }

    private fun isBackupArchive(path: Path): Boolean {
        try {
            if ("application/gzip" != Files.probeContentType(path)) {
                return false
            }

            val inputStream = GzipCompressorInputStream(BufferedInputStream(Files.newInputStream(path)))
            inputStream.use {
                val metadata = it.metaData
                return ARCHIVE_COMMENT == metadata.comment
            }
        } catch (ioe: IOException) {
            //If it's not possible to determine whether the file is a backup, do not remove it just to be safe
            println("Failed to check content type of $path -> not deleting it just to be safe (${ioe.message})")
            return false
        }
    }

    private fun createFileNameForBackup(): String {
        //FIXME: this will delete the old file if it has the same name
        return "${directory.fileName}_${LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)}.tar.gz"
    }

    fun takeBackup() {
        removeOldestArchives()
        val fileName = createFileNameForBackup()
        println("Creating backup to $fileName")
        Archiver.compressDirectory(directory, outputDirectory.resolve(fileName), GzipParameters().apply {
            comment = ARCHIVE_COMMENT
        })
    }
}