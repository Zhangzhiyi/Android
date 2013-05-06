package com.et.TestPullXML;

public class Person  
{  
      
    private Integer id;  
    private String name;  
    private Short age;  
      
    public Person()  
    {  
          
    }  
      
    public Person(Integer id, String name, Short age)  
    {  
        super();  
        this.id = id;  
        this.name = name;  
        this.age = age;  
    }  
    public Integer getId()  
    {  
        return id;  
    }  
    public void setId(Integer id)  
    {  
        this.id = id;  
    }  
    public String getName()  
    {  
        return name;  
    }  
    public void setName(String name)  
    {  
        this.name = name;  
    }  
    public Short getAge()  
    {  
        return age;  
    }  
    public void setAge(Short age)  
    {  
        this.age = age;  
    }  
    @Override  
    public String toString()  
    {  
        return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";  
    }  
}  