package com.wingrez.lovelypet.bean;

public class Pet {
    private String name; //姓名
    private int age; //年龄
    private int gender; //性别
    private int kind; //种类 0->狗 1->猫

    private String master; //主人

    private int level; //等级
    private int experience; //经验/成长值

    private int hungry; //饥饿值
    private int cleaness; //清洁值
    private int happiness; //幸福值

//    //保留的属性
//    private String achievemrnt; //成就
//    private int wisdom; //智慧
//    private int agility; //敏捷
//    private int attack; //攻击
//    private int defense; //防御

    private int mood; //宠物情绪 0->无聊 1->害羞 2->开心 3->伤心 4->生气
    private int state; //宠物状态 0->健康 1->饥饿 2->不堪 3->生病 4->逝世


    public Pet(String name,int gender, int kind){
        setName(name);
        setAge(0);
        setGender(gender);
        setKind(kind);
        setMaster("Master");
        setLevel(0);
        setExperience(0);
        setHungry(100);
        setCleaness(100);
        setHappiness(100);
        setMood(0);
        setState(0);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public int getLevel() {
        return level;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getHungry() {
        return hungry;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public int getCleaness() {
        return cleaness;
    }

    public void setCleaness(int cleaness) {
        this.cleaness = cleaness;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
