package com.zzcm.tmp.bean;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/12/22.
 */
@Entity
@Table(name="t_node")
public class Node {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String ip;
    private String user;
    private String pwd;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "from")
    //private Collection<Task> fromTasks;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "to")
    //private Collection<Task> toTasks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        if(null==name) return ip;
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
