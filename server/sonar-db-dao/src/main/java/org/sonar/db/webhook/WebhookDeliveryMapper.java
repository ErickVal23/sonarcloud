/*
 * SonarQube
 * Copyright (C) 2009-2023 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.db.webhook;

import java.util.List;
import javax.annotation.CheckForNull;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface WebhookDeliveryMapper {

  @CheckForNull
  WebhookDeliveryDto selectByUuid(@Param("uuid") String uuid);

  int countByWebhookUuid(@Param("webhookUuid") String webhookUuid);

  List<WebhookDeliveryLiteDto> selectByWebhookUuid(@Param("webhookUuid") String webhookUuid, RowBounds rowBounds);

  int countByProjectUuid(@Param("projectUuid") String projectUuid);

  List<WebhookDeliveryLiteDto> selectOrderedByProjectUuid(@Param("projectUuid") String projectUuid, RowBounds rowBounds);

  int countByCeTaskUuid(@Param("ceTaskUuid") String ceTaskId);

  List<WebhookDeliveryLiteDto> selectOrderedByCeTaskUuid(@Param("ceTaskUuid") String ceTaskUuid, RowBounds rowBounds);

  void insert(WebhookDeliveryDto dto);

  void deleteProjectBeforeDate(@Param("projectUuid") String projectUuid, @Param("beforeDate") long beforeDate);

  void deleteByWebhookUuid(@Param("webhookUuid") String webhookUuid);
}
