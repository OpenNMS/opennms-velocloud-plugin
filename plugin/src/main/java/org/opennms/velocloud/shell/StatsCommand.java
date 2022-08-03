package org.opennms.velocloud.shell;

import java.util.concurrent.TimeUnit;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

@Command(scope = "opennms-velocloud", name = "stats", description = "Show statistics.")
@Service
public class StatsCommand implements Action {

    @Override
    public Object execute() {
        final MetricRegistry metrics = null;
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.report();
        return null;
    }
}
