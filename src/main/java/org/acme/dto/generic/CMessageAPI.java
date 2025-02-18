package org.acme.dto.generic;

public class CMessageAPI implements IMessageAPI {
    private final String msgName;
    private final Class<?> typeInput;
    private Object output;

    public CMessageAPI(String msgName, Class<?> typeInput) {
        this.msgName = msgName;
        this.typeInput = typeInput;
    }

    @Override
    public String getMsgName() {
        return msgName;
    }

    @Override
    public Class<?> getTypeInput() {
        return typeInput;
    }

    @Override
    public void setOutput(Object output) {
        this.output = output;
    }

    @Override
    public Object getOutput() {
        return output;
    }
}
