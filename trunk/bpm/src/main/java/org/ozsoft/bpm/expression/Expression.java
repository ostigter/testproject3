package org.ozsoft.bpm.expression;

import org.ozsoft.bpm.ProcessContext;
import org.ozsoft.bpm.exception.BpmExpressionException;

public interface Expression {
	
	Object evaluate(ProcessContext context) throws BpmExpressionException;

}
