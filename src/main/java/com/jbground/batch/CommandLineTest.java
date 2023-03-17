package com.jbground.batch;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

public class CommandLineTest extends CommandLineJobRunner {
    public static void main(String[] args) throws Exception {
        CommandLineJobRunner.main(args);
    }
}
