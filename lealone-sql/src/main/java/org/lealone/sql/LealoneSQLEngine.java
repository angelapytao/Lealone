/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.sql;

import org.lealone.db.CommandParameter;
import org.lealone.db.Constants;
import org.lealone.db.PluginBase;
import org.lealone.db.schema.Sequence;
import org.lealone.db.session.ServerSession;
import org.lealone.db.session.Session;
import org.lealone.db.value.Value;
import org.lealone.sql.expression.Expression;
import org.lealone.sql.expression.Parameter;
import org.lealone.sql.expression.SequenceValue;
import org.lealone.sql.expression.ValueExpression;
import org.lealone.sql.expression.condition.ConditionAndOr;

public class LealoneSQLEngine extends PluginBase implements SQLEngine {

    public LealoneSQLEngine() {
        super(Constants.DEFAULT_SQL_ENGINE_NAME);
    }

    @Override
    public SQLParser createParser(Session session) {
        return new LealoneSQLParser((ServerSession) session);
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return LealoneSQLParser.quoteIdentifier(identifier);
    }

    @Override
    public CommandParameter createParameter(int index) {
        return new Parameter(index);
    }

    @Override
    public IExpression createValueExpression(Value value) {
        return ValueExpression.get(value);
    }

    @Override
    public IExpression createSequenceValue(Object sequence) {
        return new SequenceValue((Sequence) sequence);
    }

    @Override
    public IExpression createConditionAndOr(boolean and, IExpression left, IExpression right) {
        return new ConditionAndOr(and ? ConditionAndOr.AND : ConditionAndOr.OR, (Expression) left,
                (Expression) right);
    }
}
