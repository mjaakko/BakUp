package xyz.malkki.bakup.xyz.malkki.bakup

import org.junit.Test
import xyz.malkki.bakup.Archiver
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

class ArchiverTest {
    @Test
    fun `Test creating archive file`() {
        val directory = Files.createTempDirectory("archiver_test")
        val file1 = directory.resolve("file1.txt")
        val file2 = directory.resolve("file2.txt")
        val archive = Files.createTempFile("archiver_test", "tar.gz")

        try {
            Files.write(file1, "test".toByteArray())
            Files.write(file2, "test".toByteArray())

            Archiver.compressDirectory(directory, archive)

            assertTrue(Files.exists(archive))
            assertTrue(Files.size(archive) > 0)
        } finally {
            deleteQuietly(archive)
            deleteQuietly(file1)
            deleteQuietly(file2)
            deleteQuietly(directory)
        }
    }

    private fun deleteQuietly(path: Path) {
        try {
            Files.deleteIfExists(path)
        } catch (e: Exception) {
            println("Failed to delete $path")
        }
    }
}