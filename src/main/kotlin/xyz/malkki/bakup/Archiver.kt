package xyz.malkki.bakup

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.compressors.gzip.GzipParameters
import java.io.BufferedOutputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

object Archiver {
    fun compressDirectory(directory: Path, outputFile: Path, gzipParameters: GzipParameters? = null) {
        if (Files.notExists(directory) || !Files.isDirectory(directory)) {
            println("Directory ${directory.fileName} not found")
        }

        val fileOutputStream = BufferedOutputStream(Files.newOutputStream(outputFile))
        val gzipOutputStream = if (gzipParameters != null) { GzipCompressorOutputStream(fileOutputStream, gzipParameters) } else { GzipCompressorOutputStream(fileOutputStream) }

        val tarOutputStream = TarArchiveOutputStream(gzipOutputStream)
        tarOutputStream.use { outputStream ->
            Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
                private fun getEntryName(path: Path): String {
                    return directory.relativize(path).toString()
                }

                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val entryName = getEntryName(dir)
                    if (entryName != "") {
                        val entry = TarArchiveEntry(dir.toFile(), entryName)
                        outputStream.putArchiveEntry(entry)
                        outputStream.closeArchiveEntry()
                    }
                    return FileVisitResult.CONTINUE
                }

                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val entryName = getEntryName(file)
                    val entry = TarArchiveEntry(file.toFile(), entryName)
                    outputStream.putArchiveEntry(entry)
                    Files.copy(file, outputStream)
                    outputStream.closeArchiveEntry()
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }
}