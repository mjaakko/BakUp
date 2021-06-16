package xyz.malkki.bakup

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@ExperimentalTime
class BackupManagerTest {
    @Rule @JvmField
    val folder = TemporaryFolder()

    private lateinit var outputDirectory: File
    private lateinit var backupManager: BackupManager

    @Before
    fun initialize() {
        val backupDirectory = folder.newFolder("backup")
        val a = File(backupDirectory, "a.txt")
        a.writeText("a")
        val b = File(backupDirectory, "b.txt")
        b.writeText("b")

        outputDirectory = folder.newFolder("output")

        backupManager = BackupManager(backupDirectory.toPath(), outputDirectory.toPath(), 3)
    }

    @Test
    fun `Test that correct amount of backup archives is created`() {
        backupManager.takeBackup()
        assertEquals(1, outputDirectory.list().size)

        Thread.sleep(1500)

        backupManager.takeBackup()
        assertEquals(2, outputDirectory.list().size)

        Thread.sleep(1500)

        backupManager.takeBackup()
        assertEquals(3, outputDirectory.list().size)

        Thread.sleep(1500)

        //Max 3 backups
        backupManager.takeBackup()
        assertEquals(3, outputDirectory.list().size)
    }

    @Test
    fun `Test that other files are not deleted from output directory`() {
        val archiveFile = createArchive(outputDirectory)
        val testFile = File(outputDirectory, "test.txt")
        testFile.writeText("test")

        backupManager.takeBackup()
        Thread.sleep(1500)
        backupManager.takeBackup()
        Thread.sleep(1500)
        backupManager.takeBackup()
        Thread.sleep(1500)
        backupManager.takeBackup()

        assertTrue(archiveFile.exists())
        assertTrue(testFile.exists())
    }

    /**
     * Creates an archive file to output directory to test that unrelated archives are not deleted by BackupManager
     */
    private fun createArchive(directory: File): File {
        val archiveContent = folder.newFile("test.txt")
        archiveContent.writeText("test")

        val archiveFile = File(directory, "test.tar.gz")
        val outputStream = TarArchiveOutputStream(GzipCompressorOutputStream(BufferedOutputStream(FileOutputStream(archiveFile))))
        outputStream.use {
            val entry = TarArchiveEntry(archiveContent, "test.txt")
            it.putArchiveEntry(entry)
            Files.copy(archiveContent.toPath(), it)
            it.closeArchiveEntry()
        }
        return archiveFile
    }
}