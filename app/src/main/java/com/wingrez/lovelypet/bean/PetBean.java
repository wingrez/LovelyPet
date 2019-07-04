package com.wingrez.lovelypet.bean;

public class PetBean {
    private int id; //宠物id
    private String petName; //姓名
    private int gender; //性别 0公 1母
    private int age; //年龄
    private int level; //等级
    private int kind; //种类 0->狗 1->猫

    private String hostNmae; //主人

    private int experience; //经验/成长值，数值
    private int hungry; //饥饿值，数值
    private int cleaness; //清洁值，数值
    private int happiness; //幸福值，百分数值

    private int allExperience;
    private int allHungry;
    private int allCleaness;
    private int allHappiness;

//    //保留的属性
//    private String achievemrnt; //成就
//    private int wisdom; //智慧
//    private int agility; //敏捷
//    private int attack; //攻击
//    private int defense; //防御

    private int mood; //宠物情绪 0->无聊 1->害羞 2->开心 3->伤心 4->生气
    private int state; //宠物状态 0->健康 1->饥饿 2->不堪 3->生病 4->逝世


    public PetBean(String petName, String hostNmae, int gender, int kind,int allHungry, int allCleaness, int allHappiness) {
        setPetName(petName);
        setAge(0);
        setLevel(0);
        setGender(gender);
        setKind(kind);

        setHostNmae(hostNmae);

        setExperience(0);
        setHungry(allHungry);
        setCleaness(allCleaness);
        setHappiness(allHappiness);


        setMood(0);
        setState(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getHostNmae() {
        return hostNmae;
    }

    public void setHostNmae(String hostNmae) {
        this.hostNmae = hostNmae;
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

    public int getAllExperience() {
        return allExperience;
    }

    public void setAllExperience(int allExperience) {
        this.allExperience = allExperience;
    }

    public int getAllHungry() {
        return allHungry;
    }

    public void setAllHungry(int allHungry) {
        this.allHungry = allHungry;
    }

    public int getAllCleaness() {
        return allCleaness;
    }

    public void setAllCleaness(int allCleaness) {
        this.allCleaness = allCleaness;
    }

    public int getAllHappiness() {
        return allHappiness;
    }

    public void setAllHappiness(int allHappiness) {
        this.allHappiness = allHappiness;
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

    private boolean canEatFood() {
        if (state==4 || hungry == allHungry) return false;
        return true;
    }

    private boolean canClean(){
        if(state==4 || cleaness==allCleaness) return false;
        return true;
    }

    public int eat(FoodBean food) {
        if (canEatFood()) {
            hungry+=food.getRefresh();
            if(hungry>=allHungry) hungry=allHungry;
            return 201; //吃食物成功
        }
        return 202; //吃食物失败，
    }

    public int clean(CleanserBean cleanser){
        if(canClean()){
            cleaness+=cleanser.getRefresh();
            if(cleaness>=allCleaness) cleaness=allCleaness;
            return 301; //清洁成功
        }
        return 302; //清洁失败
    }

    private boolean canFun(){
        if(state==3 || state==4) return false;
        return true;
    }

    public int fun(GameBean game){
        if(canFun()){
            happiness+=game.getRefresh();
            if(happiness>allHappiness) happiness=allHappiness;
            return 401; //游戏成功
        }
        return 402; //游戏失败
    }



}
