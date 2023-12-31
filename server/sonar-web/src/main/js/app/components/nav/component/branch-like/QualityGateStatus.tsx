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
import classNames from 'classnames';
import { QualityGateIndicator } from 'design-system';
import React, { useContext } from 'react';
import { getBranchStatusByBranchLike } from '../../../../../helpers/branch-like';
import { translateWithParameters } from '../../../../../helpers/l10n';
import { formatMeasure } from '../../../../../helpers/measures';
import { BranchLike } from '../../../../../types/branch-like';
import { MetricType } from '../../../../../types/metrics';
import { Component } from '../../../../../types/types';
import { BranchStatusContext } from '../../../branch-status/BranchStatusContext';

interface Props {
  component: Component;
  branchLike: BranchLike;
  className: string;
  showStatusText?: boolean;
}

export default function QualityGateStatus({
  component,
  branchLike,
  className,
  showStatusText,
}: Props) {
  const { branchStatusByComponent } = useContext(BranchStatusContext);
  const branchStatus = getBranchStatusByBranchLike(
    branchStatusByComponent,
    component.key,
    branchLike
  );

  // eslint-disable-next-line @typescript-eslint/prefer-optional-chain, @typescript-eslint/no-unnecessary-condition
  if (!branchStatus || !branchStatus.status) {
    return null;
  }
  const { status } = branchStatus;
  const formatted = formatMeasure(status, MetricType.Level);
  const ariaLabel = translateWithParameters('overview.quality_gate_x', formatted);
  return (
    <div className={classNames(`it__level-${status}`, className)}>
      <QualityGateIndicator status={status} className="sw-mr-2" ariaLabel={ariaLabel} size="sm" />
      {showStatusText && <span>{formatted}</span>}
    </div>
  );
}
