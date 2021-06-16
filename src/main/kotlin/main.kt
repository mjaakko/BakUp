
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import xyz.malkki.bakup.BackupManager
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main(args: Array<String>) {
    val cliOptions = Options().apply {
        addOption("d", "directory", true, "The directory which will be backed up")
        addOption("o", "outputDirectory", true, "The directory where the backup archives are stored")
        addOption("n", "maxBackups", true, "The maximum number of backups that will be stored")
        addOption("h", "help", false, "Print this message")
    }
    val optionsParser = DefaultParser()
    val cli = optionsParser.parse(cliOptions, args)

    //Show help message
    val showHelp = cli.options.isEmpty() || cli.hasOption("h")
    if (showHelp) {
        val helpFormatter = HelpFormatter()
        helpFormatter.printHelp("bakup [OPTIONS]", cliOptions)
        return
    }

    val directory = Paths.get(cli.getOptionValue("d"))
    val outputDirectory = Paths.get(cli.getOptionValue("o"))
    val maxBackups = cli.getOptionValue("n").toInt()

    try {
        Files.createDirectories(outputDirectory)
    } catch (e: Exception) {
        println("Failed to create output directory for backup archives: ${e.message}")
        return
    }

    val backupManager = BackupManager(directory, outputDirectory, maxBackups)
    backupManager.takeBackup()
}