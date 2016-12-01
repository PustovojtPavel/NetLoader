package org.example;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.args4j.Option;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApplicationSettings implements Serializable {

    @Option(name = "-file", usage = "Set path to file with download list", required = true)
    private String fileName;

    @Option(name = "-threadCount",
        usage = "Set thread count which will processing download. Default value = 1, max value = 10")
    private Integer threadCount = 1;
}
