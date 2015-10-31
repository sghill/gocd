/*************************GO-LICENSE-START*********************************
 * Copyright 2014 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *************************GO-LICENSE-END***********************************/

package com.thoughtworks.go.util.validators;

import java.io.File;

import com.thoughtworks.go.util.SystemEnvironment;
import com.thoughtworks.go.util.TestFileUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.thoughtworks.go.util.OperatingSystem.LINUX;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoggingValidatorTest {
    private File log4jPropertiesFile;
    @Mock
    private Validator log4jFileValidator;
    @Mock
    private LogDirectory logDirectory;
    @Mock
    private LoggingValidator.Log4jConfigReloader log4jConfigReloader;
    @Mock
    private SystemEnvironment env;

    @Before
    public void setUp() throws Exception {
        log4jPropertiesFile = new File("log4j.properties");
        initMocks(this);
    }

    @Test
    public void shouldValidateLog4jExistsAndUpdateLogFolder() throws Exception {
        final Validation validation = new Validation();
        given(log4jFileValidator.validate(validation)).willReturn(validation);
        given(logDirectory.update(log4jPropertiesFile, validation)).willReturn(validation);
        LoggingValidator loggingValidator = new LoggingValidator(log4jPropertiesFile, log4jFileValidator, logDirectory, log4jConfigReloader);

        loggingValidator.validate(validation);

        assertThat(validation.isSuccessful(), is(true));
        verify(log4jConfigReloader).reload(log4jPropertiesFile);
    }

    @Test
    public void shouldCreateObjectCorrectly() throws Exception {
        File tempFolder = TestFileUtil.createUniqueTempFolder("foo");
        final String path = tempFolder.getAbsolutePath();
        given(env.getConfigDir()).willReturn(path);
        given(env.getCurrentOperatingSystem()).willReturn(LINUX);

        LoggingValidator validator = new LoggingValidator(env);

        assertThat(validator.getLog4jFile(), is(new File(tempFolder, "log4j.properties")));
        assertThat((FileValidator) validator.getLog4jPropertiesValidator(), is(FileValidator.configFile("log4j.properties", env)));
        assertThat(validator.getLogDirectory(), is(LogDirectory.fromEnvironment(LINUX)));
    }
}
