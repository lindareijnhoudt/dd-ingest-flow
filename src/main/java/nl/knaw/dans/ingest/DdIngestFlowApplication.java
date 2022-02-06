/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.knaw.dans.ingest;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.dans.ingest.core.config.DataverseConfigScala;
import nl.knaw.dans.ingest.core.legacy.DepositIngestTaskFactoryWrapper;
import nl.knaw.dans.ingest.core.sequencing.TargettedTaskSequenceManager;
import nl.knaw.dans.lib.dataverse.DataverseInstance;
import nl.knaw.dans.lib.dataverse.DataverseInstanceConfig;
import scala.Option;

import java.util.concurrent.ExecutorService;

public class DdIngestFlowApplication extends Application<DdIngestFlowConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DdIngestFlowApplication().run(args);
    }

    @Override
    public String getName() {
        return "DD Ingest Flow";
    }

    @Override
    public void initialize(final Bootstrap<DdIngestFlowConfiguration> bootstrap) {
    }

    @Override
    public void run(final DdIngestFlowConfiguration configuration, final Environment environment) {
        final ExecutorService taskExecutor = configuration.getImportConf().getTaskQueue().build(environment);
        final TargettedTaskSequenceManager targettedTaskSequenceManager = new TargettedTaskSequenceManager(taskExecutor);
//        final ManagedInbox autoIngestInbox = new ManagedInbox(configuration.getAutoIngest().getInbox(), depositSequenceManager);
//        environment.lifecycle().manage(autoIngestInbox);

        final DepositIngestTaskFactoryWrapper importTaskFactoryWrapper = new DepositIngestTaskFactoryWrapper(
            configuration.getImportConf(),
            configuration.getDataverse(),
            configuration.getManagePrestaging(),
            configuration.getValidateDansBag());

        // pass to import resource

    }

}
