package org.acme.dto.generic;

public interface IMessageAPI {
    String getMsgName();

    Class<?> getTypeInput();

    void setOutput(Object output);

    Object getOutput();
}
