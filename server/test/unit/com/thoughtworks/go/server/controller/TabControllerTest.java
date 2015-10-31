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

package com.thoughtworks.go.server.controller;

import java.io.IOException;

import com.thoughtworks.go.config.BasicPipelineConfigs;
import com.thoughtworks.go.config.CaseInsensitiveString;
import com.thoughtworks.go.i18n.Localizer;
import com.thoughtworks.go.server.domain.Username;
import com.thoughtworks.go.server.service.GoConfigService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class TabControllerTest {
    private Localizer localizer;
    @Mock
    private GoConfigService service;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnModelAndViewWhenPipelinesArePresent() throws IOException {
        given(service.isPipelineEmpty()).willReturn(true);
        TabController controller = new TabController(service, localizer);

        ModelAndView modelAndView = controller.handleOldPipelineTab(new MockHttpServletRequest(),
                new MockHttpServletResponse());

        assertThat(modelAndView.getViewName(), is(TabController.TAB_PLACEHOLDER));
    }

    @Test
    public void shouldReturnModelAndViewWithNoPipelinesAndNonAdminUser() throws IOException {
        given(service.isPipelineEmpty()).willReturn(true);
        given(service.isAdministrator(CaseInsensitiveString.str(Username.ANONYMOUS.getUsername()))).willReturn(false);
        TabController controller = new TabController(service, localizer);

        ModelAndView modelAndView = controller.handleOldPipelineTab(new MockHttpServletRequest(),
                new MockHttpServletResponse());

        assertThat(modelAndView.getViewName(), is(TabController.TAB_PLACEHOLDER));
    }

    @Test
    public void shouldRedirectToHomePage() throws Exception {
        TabController controller = new TabController(null, localizer);
        StubResponse response = new StubResponse();
        controller.handlePipelineTab(new MockHttpServletRequest(),response);
        assertThat(response.url, is("/go/home"));
    }

    @Test
    public void shouldRedirectWhenNoPipelinesAndAdminUser() throws IOException {
        given(service.isPipelineEmpty()).willReturn(true);
        given(service.isAdministrator(CaseInsensitiveString.str(Username.ANONYMOUS.getUsername()))).willReturn(true);
        TabController controller = new TabController(service, localizer);
        StubResponse response = new StubResponse();
        assertThat(controller.handleOldPipelineTab(new MockHttpServletRequest(), response), is(nullValue()));
        assertThat(response.url, is(String.format("admin/pipeline/new?group=%s", BasicPipelineConfigs.DEFAULT_GROUP)));
    }

    private class StubResponse extends MockHttpServletResponse {
        String url;

        public void sendRedirect(String url) throws IOException {
            this.url = url;
        }
    }
}
