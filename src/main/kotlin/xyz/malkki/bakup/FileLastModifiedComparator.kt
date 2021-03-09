package xyz.malkki.bakup

import java.nio.file.Files
import java.nio.file.Path

object FileLastModifiedComparator : Comparator<Path> {
    override fun compare(path1: Path, path2: Path): Int {
        return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2))
    }
}