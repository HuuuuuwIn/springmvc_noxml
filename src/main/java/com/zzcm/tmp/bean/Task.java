package com.zzcm.tmp.bean;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/12/22.
 */
@Entity
@Table(name = "t_task")
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(name = "group_name")
    private String group;

    @JoinColumn(name = "n_from",referencedColumnName = "id")
    @ManyToOne
    private Node from;
    @JoinColumn(name = "n_to",referencedColumnName = "id")
    @ManyToOne
    private Node to;
    @Column(name = "crontime")
    private String cronTime;

    private Boolean enable;

    private Boolean sync;

    @Column(name = "dir_from")
    private String fromDir;
    @Column(name = "filename")
    private String fileName;
    @Column(name = "dir_to")
    private String toDir;

    //@Transient
    @Enumerated(value = EnumType.ORDINAL)
    private Status status = Status.STOP;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public String getCronTime() {
        return cronTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getGroup() {
        if(null==group) return "group_"+id;
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getSync() {
        return sync;
    }

    public void setSync(Boolean sync) {
        this.sync = sync;
    }

    public String getFromDir() {
        return fromDir;
    }

    public void setFromDir(String fromDir) {
        this.fromDir = fromDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getToDir() {
        return toDir;
    }

    public void setToDir(String toDir) {
        this.toDir = toDir;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", cronTime='" + cronTime + '\'' +
                ", enable=" + enable +
                ", sync=" + sync +
                ", fromDir='" + fromDir + '\'' +
                ", fileName='" + fileName + '\'' +
                ", toDir='" + toDir + '\'' +
                '}';
    }

    public static enum Status{
        STOP(0,"停止"),
        RUN(1,"运行"),
        PAUSE(2,"暂停");
        private int code;
        private String desc;
        private Status(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
