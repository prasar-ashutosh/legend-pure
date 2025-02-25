// Copyright 2020 Goldman Sachs
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.finos.legend.pure.runtime.java.compiled.generation.processors.natives.core.lang;

import org.eclipse.collections.api.list.ListIterable;
import org.finos.legend.pure.m3.navigation.Instance;
import org.finos.legend.pure.m3.navigation.M3Properties;
import org.finos.legend.pure.m3.navigation.ProcessorSupport;
import org.finos.legend.pure.m3.navigation.multiplicity.Multiplicity;
import org.finos.legend.pure.m4.coreinstance.CoreInstance;
import org.finos.legend.pure.runtime.java.compiled.generation.ProcessorContext;
import org.finos.legend.pure.runtime.java.compiled.generation.processors.natives.AbstractNative;
import org.finos.legend.pure.runtime.java.compiled.generation.processors.type.FullJavaPaths;
import org.finos.legend.pure.runtime.java.compiled.generation.processors.type.TypeProcessor;

public class Evaluate extends AbstractNative
{
    public Evaluate()
    {
        super("evaluate_Function_1__List_MANY__Any_MANY_");
    }

    @Override
    public String build(CoreInstance topLevelElement, CoreInstance functionExpression, ListIterable<String> transformedParams, ProcessorContext processorContext)
    {
        ProcessorSupport processorSupport = processorContext.getSupport();
        ListIterable<? extends CoreInstance> parametersValues = Instance.getValueForMetaPropertyToManyResolved(functionExpression, M3Properties.parametersValues, processorContext.getSupport());

        String type = TypeProcessor.typeToJavaObjectWithMul(Instance.getValueForMetaPropertyToOneResolved(functionExpression, M3Properties.genericType, processorSupport), Instance.getValueForMetaPropertyToOneResolved(functionExpression, M3Properties.multiplicity, processorSupport), processorSupport);
        CoreInstance multiplicity = Instance.getValueForMetaPropertyToOneResolved(parametersValues.get(1), M3Properties.multiplicity, processorSupport);
        String param = transformedParams.get(1);

        return "((" + type + ")(Object)(CompiledSupport.toPureCollection(CoreGen.evaluateToMany(es, " + transformedParams.get(0) + ", " + (Multiplicity.isToOne(multiplicity, false) ? "CompiledSupport.toPureCollection(" + param + ")" : param) + "))))";
    }

    @Override
    public String buildBody()
    {

        return "new PureFunction2<" + FullJavaPaths.Function + ", Object, Object>()\n" +
                "        {\n" +
                "            @Override\n" +
                "            public Object value(" + FullJavaPaths.Function + " func, Object params, ExecutionSupport es)\n" +
                "            {\n" +
                "                return CompiledSupport.toPureCollection(\n" +
                "                       CoreGen.evaluateToMany(es, func, params instanceof RichIterable || params == null ? CompiledSupport.toPureCollection((ListIterable<" + FullJavaPaths.List + ">) params) : Lists.mutable.with((" + FullJavaPaths.List + ")params))\n" +
                "                       );\n" +
                "            }\n" +
                "        }";
    }
}
