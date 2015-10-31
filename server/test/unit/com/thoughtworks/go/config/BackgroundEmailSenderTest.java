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

package com.thoughtworks.go.config;

import com.thoughtworks.go.domain.materials.ValidationBean;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.StringContains.containsString;

import com.thoughtworks.go.server.messaging.SendEmailMessage;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class BackgroundEmailSenderTest {

    GoMailSender neverReturns = new GoMailSender() {
        public ValidationBean send(String subject, String body, String to) {
            try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        public ValidationBean send(SendEmailMessage message) {
            return send(message.getSubject(), message.getBody(), message.getTo());
        }

    };
    @Mock
    private GoMailSender sender;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnNotValidIfSendingTimesout() {
        BackgroundMailSender background = new BackgroundMailSender(neverReturns, 1);
        ValidationBean validationBean = background.send("Subject", "body", "to@someone");
        assertThat(validationBean.isValid(), is(false));
        assertThat(validationBean.getError(), containsString("Timeout when sending email"));
    }

    @Test
    public void shouldReturnWhateverTheOtherSenderReturnsIfSendingDoesNotTimeout() {
        final ValidationBean validationBean = ValidationBean.valid();
        given(sender.send("Subject", "body", "to@someone")).willReturn(validationBean);
        BackgroundMailSender background = new BackgroundMailSender(sender, 1000);

        ValidationBean returned = background.send("Subject", "body", "to@someone");

        assertThat(returned, sameInstance(validationBean));
    }

}
