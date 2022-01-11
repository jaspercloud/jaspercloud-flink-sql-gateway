/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ververica.flink.table.gateway.operation;

import com.ververica.flink.table.gateway.context.ExecutionContext;
import com.ververica.flink.table.gateway.context.SessionContext;
import com.ververica.flink.table.gateway.rest.result.ConstantNames;
import com.ververica.flink.table.gateway.rest.result.ResultSet;

import org.apache.flink.table.api.TableEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Operation for SHOW DATABASES command.
 */
public class ShowDatabasesOperation implements NonJobOperation {

    private final ExecutionContext<?> context;

    public ShowDatabasesOperation(SessionContext context) {
        this.context = context.getExecutionContext();
    }

    @Override
    public ResultSet execute() {
        final TableEnvironment tableEnv = context.getTableEnvironment();
        final List<String> databases = context.wrapClassLoader(() -> {
            String catalog = tableEnv.getCurrentCatalog();
            List<String> dbList = Arrays.asList(tableEnv.listDatabases()).stream()
                    .map(e -> String.format("%s.%s", catalog, e))
                    .collect(Collectors.toList());
            return dbList;
        });
        return OperationUtil.stringListToResultSet(databases, ConstantNames.SHOW_DATABASES_RESULT);
    }
}
