package rong;

public class FunSymbol {
    private String m_name;
    private String m_value;
    private int m_type;
    public FunSymbol(String name){
        m_name=name;
    }

    public String GetName(){
        return m_name;
    }

    public void SetName(String name){
        m_name = name;
    }

    public String getValue(){
        return m_value;
    }

    public void setValue(String value){
        m_value = value;
    }

    public int getType(){
        return m_type;
    }

    public void setType(int type){
        m_type=type;
    }
}
