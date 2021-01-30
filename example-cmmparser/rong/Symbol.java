package rong;

public class Symbol {

    private String m_name;
    private int m_level;
    private String m_value;
    private int m_type;
    //type=0 integer  1 real  3 string
    public Symbol(int level,String name){
        m_name=name;
        m_level=level;
    }

    public String GetName(){
        return m_name;
    }

    public void SetName(String name){
        m_name = name;
    }

    public int GetLevel(){
        return m_level;
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
