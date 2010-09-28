/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.core.plugin;

import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.sonar.api.BatchExtension;
import org.sonar.api.ExtensionProvider;
import org.sonar.api.Plugin;
import org.sonar.api.ServerExtension;
import org.sonar.api.utils.IocContainer;

import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractPluginRepositoryTest {

  @Test
  public void testIsType() {
    assertThat(AbstractPluginRepository.isType(FakeServerExtension.class, ServerExtension.class), is(true));
    assertThat(AbstractPluginRepository.isType(new FakeServerExtension(), ServerExtension.class), is(true));

    assertThat(AbstractPluginRepository.isType(FakeBatchExtension.class, ServerExtension.class), is(false));
    assertThat(AbstractPluginRepository.isType(new FakeBatchExtension(), ServerExtension.class), is(false));
    assertThat(AbstractPluginRepository.isType(String.class, ServerExtension.class), is(false));
    assertThat(AbstractPluginRepository.isType("foo", ServerExtension.class), is(false));
  }

  @Test
  public void extensionKeyshouldBeClassNameIfClass() {
    assertEquals(AbstractPluginRepository.getExtensionKey(FakeServerExtension.class), FakeServerExtension.class);
  }

  @Test
  public void extensionKeyshouldBeUniqueIfObject() {
    assertThat((String) AbstractPluginRepository.getExtensionKey(new FakeServerExtension()), endsWith("FakeServerExtension-instance"));
  }

  @Test
  public void extensionProviderIsAnObjectButNotAClass() {
    assertThat(AbstractPluginRepository.isExtensionProvider(BProvider.class), is(false));
    assertThat(AbstractPluginRepository.isExtensionProvider(new BProvider()), is(true));
  }

  @Test
  public void shouldRegisterExtensionProviders() {
    MutablePicoContainer pico = IocContainer.buildPicoContainer();
    AbstractPluginRepository repository = new AbstractPluginRepository() {
      @Override
      protected boolean shouldRegisterExtension(String pluginKey, Object extension) {
        return isType(extension, ServerExtension.class);
      }
    };

    Plugin plugin = mock(Plugin.class);
    BProvider bProvider = new BProvider();
    when(plugin.getExtensions()).thenReturn(Arrays.asList(A.class, bProvider, C.class, D.class));
    repository.registerPlugin(pico, plugin, "foo");
    pico.start();

    assertThat(pico.getComponent(A.class), is(A.class));
    assertThat(pico.getComponent(C.class), is(C.class));
    assertThat(pico.getComponent(D.class), is(D.class));
    assertThat(pico.getComponent(C.class).getBees().length, is(2));
    assertThat(pico.getComponent(D.class).getBees().length, is(2));
    assertThat(bProvider.calls, is(1)); // do not create B instances two times (C and D dependencies)

    // Picocontainer question: why components created by providers are not registered in the container ?
    //assertThat(pico.getComponents(B.class).size(), is(2)); // created by the ExtensionProvider
  }

  public static class FakeServerExtension implements ServerExtension {
    @Override
    public String toString() {
      return "instance";
    }
  }

  public static class FakeBatchExtension implements BatchExtension {

  }

  public static class A implements ServerExtension {
  }

  public static class B implements ServerExtension {
    private A a;

    public B(A a) {
      this.a = a;
    }
  }

  public static class C implements ServerExtension {
    private B[] bees;

    public C(B[] bees) {
      this.bees = bees;
    }

    public B[] getBees() {
      return bees;
    }
  }

  public static class D implements ServerExtension {
    private B[] bees;

    public D(B[] bees) {
      this.bees = bees;
    }

    public B[] getBees() {
      return bees;
    }
  }

  public static class BProvider extends ExtensionProvider {

    private int calls = 0;
    public B[] provide(A a) {
      calls++;
      return new B[]{new B(a), new B(a)};
    }
  }


}
