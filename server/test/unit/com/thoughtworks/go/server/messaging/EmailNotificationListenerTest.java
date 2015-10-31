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

package com.thoughtworks.go.server.messaging;

import com.thoughtworks.go.config.*;
import com.thoughtworks.go.domain.materials.ValidationBean;
import com.thoughtworks.go.helper.GoConfigMother;
import com.thoughtworks.go.server.service.GoConfigService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EmailNotificationListenerTest {
    @Mock
    private GoConfigService goConfigService;
    @Mock
    private EmailNotificationListener.GoMailSenderFactory goMailSenderFactory;
    @Mock
    private GoMailSender sender;
    private EmailNotificationListener emailNotificationListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        emailNotificationListener = new EmailNotificationListener(goConfigService, goMailSenderFactory);
    }

    @Test
    public void shouldNotCreateMailSenderIfMailHostIsNotConfigured() {
        given(goConfigService.currentCruiseConfig()).willReturn(new BasicCruiseConfig());
        emailNotificationListener.onMessage(null);
        verify(goMailSenderFactory, never()).createSender();
    }

    @Test
    public void shouldCreateMailSenderIfMailHostIsConfigured() {
        final MailHost mailHost = new MailHost("hostName", 1234, "user", "pass", true, true, "from", "admin@local.com");
        final CruiseConfig config = GoConfigMother.cruiseConfigWithMailHost(mailHost);
        given(goConfigService.currentCruiseConfig()).willReturn(config);
        given(goMailSenderFactory.createSender()).willReturn(sender);
        given(sender.send(anyString(), anyString(), anyString())).willReturn(ValidationBean.valid());

        emailNotificationListener.onMessage(new SendEmailMessage("subject", "body", "to"));

        verify(sender).send("subject", "body", "to");
    }

}
