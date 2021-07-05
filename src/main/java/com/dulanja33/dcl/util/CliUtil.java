package com.dulanja33.dcl.util;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

@Slf4j
public class CliUtil {
    private CliUtil() {
    }

    public static Context cliOptionHandle(String args[]) throws DclException {
        Options options = new Options();

        Option mappingPath = Option.builder()
                .longOpt("mapping")
                .argName("mappingPath")
                .hasArg()
                .desc("absolute path to mapping file")
                .build();

        Option csvPath = Option.builder()
                .longOpt("csv")
                .argName("csvPath")
                .hasArg()
                .desc("absolute path to csv file")
                .build();

        Option chunkSize = Option.builder()
                .longOpt("chunkSize")
                .argName("chunkSize")
                .hasArg()
                .desc("number of inserts per batch - default: 1")
                .build();

        Option helper = Option.builder()
                .longOpt("help")
                .argName("help")
                .desc("help")
                .build();

        options.addOption(mappingPath);
        options.addOption(csvPath);
        options.addOption(chunkSize);
        options.addOption(helper);


        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            HelpFormatter formatter = new HelpFormatter();

            Context.ContextBuilder builder = Context.builder();

            if (cmd.hasOption("help")) {
                formatter.printHelp("db-scv-loader", options);
                return null;
            }

            if (cmd.hasOption("mapping")) {
                builder.mappingPath(cmd.getOptionValue("mapping"));
            } else {
                formatter.printHelp("db-scv-loader", options);
                throw new ParseException("error parsing mapping file");
            }

            if (cmd.hasOption("csv")) {
                builder.csvPath(cmd.getOptionValue("csv"));
            } else {
                formatter.printHelp("db-scv-loader", options);
                throw new ParseException("error parsing csv file");
            }

            if (cmd.hasOption("chunkSize")) {
                String data = cmd.getOptionValue("chunkSize");
                int chunkLength = 1;
                try {
                    chunkLength = Integer.parseInt(data);
                } catch (NumberFormatException e) {
                    log.error("error parsing chunk size, setting default value: {}", 1);
                }
                builder.chunkSize(chunkLength);
            }

            return builder.build();

        } catch (ParseException e) {
            log.error("error parsing arguments: {}", e.getMessage());
            throw new DclException("error parsing arguments");
        }
    }
}
