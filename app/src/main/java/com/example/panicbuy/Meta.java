package com.example.panicbuy;


public class Meta {
    private String m_key;
    private String m_value;

    public Meta(String m_key, String m_value) {
        this.m_key = m_key;
        this.m_value = m_value;

    }

    public String getM_key() {
        return m_key;
    }

    public void setM_key(String m_key) {
        this.m_key = m_key;
    }

    public String getM_value() {
        return m_value;
    }

    public void setM_value(String m_value) {
        this.m_value = m_value;
    }


}