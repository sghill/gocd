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

package com.thoughtworks.go.agent.service;

import java.io.IOException;

import com.thoughtworks.go.agent.testhelpers.AgentCertificateMother;
import com.thoughtworks.go.config.AgentRegistrationPropertiesReader;
import com.thoughtworks.go.config.AgentRegistry;
import com.thoughtworks.go.config.GuidService;
import com.thoughtworks.go.security.AuthSSLProtocolSocketFactory;
import com.thoughtworks.go.security.Registration;
import com.thoughtworks.go.util.ClassMockery;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static com.thoughtworks.go.util.TestUtils.exists;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class SslInfrastructureServiceTest {
    private static final Logger LOGGER = Logger.getLogger(SslInfrastructureServiceTest.class);
    private SslInfrastructureService sslInfrastructureService;
    private boolean remoteCalled;
    private HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
    @Mock
    private HttpClient mockClient;
    @Mock
    private AgentRegistry mockRegistry;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        given(mockRegistry.uuid()).willReturn("uuid");
        given(mockClient.getHttpConnectionManager()).willReturn(new MultiThreadedHttpConnectionManager());
        remoteCalled = false;
        SslInfrastructureService.AGENT_CERTIFICATE_FILE.delete();
        SslInfrastructureService.AGENT_CERTIFICATE_FILE.deleteOnExit();
        Registration registration = createRegistration();
        sslInfrastructureService = new SslInfrastructureService(requesterStub(registration), mockClient, httpConnectionManagerParams);
        GuidService.storeGuid("uuid");
    }

    @After
    public void teardown() throws Exception {
        GuidService.deleteGuid();
        Protocol.unregisterProtocol("https");
        SslInfrastructureService.AGENT_CERTIFICATE_FILE.delete();
        SslInfrastructureService.AGENT_TRUST_FILE.delete();
    }

    @Test
    public void shouldInvalidateKeystore() throws Exception {
        shouldCreateSslInfrastucture();

        sslInfrastructureService.registerIfNecessary();
        assertThat(SslInfrastructureService.AGENT_CERTIFICATE_FILE, exists());
        assertRemoteCalled();

        sslInfrastructureService.registerIfNecessary();
        assertRemoteNotCalled();

        sslInfrastructureService.invalidateAgentCertificate();
        sslInfrastructureService.registerIfNecessary();
        assertRemoteCalled();
    }

    private void shouldCreateSslInfrastucture() throws Exception {
        sslInfrastructureService.createSslInfrastructure();
        Protocol protocol = Protocol.getProtocol("https");
        assertThat(protocol.getSocketFactory(), instanceOf(AuthSSLProtocolSocketFactory.class));
    }

    private void assertRemoteCalled() {
        assertThat("Remote called", remoteCalled, is(true));
        remoteCalled = false;
    }

    private void assertRemoteNotCalled() {
        assertThat("Remote not called", remoteCalled, is(false));
        remoteCalled = false;
    }

    private Registration createRegistration() {
        Registration certificates = AgentCertificateMother.agentCertificate();
        return new Registration(certificates.getPrivateKey(), certificates.getChain());
    }

    private SslInfrastructureService.RemoteRegistrationRequester requesterStub(final Registration registration) {
        final SslInfrastructureServiceTest me = this;
        return new SslInfrastructureService.RemoteRegistrationRequester(null, mockRegistry, new HttpClient()) {
            protected Registration requestRegistration(String agentHostName, AgentRegistrationPropertiesReader agentAutoRegisterProperties)
                    throws IOException, ClassNotFoundException {
                LOGGER.debug("Requesting remote registration");
                me.remoteCalled = true;
                return registration;
            }
        };
    }
}
