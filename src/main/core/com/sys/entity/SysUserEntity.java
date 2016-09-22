package com.sys.entity;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_user")
@Filter(name="depFilter",condition = "dep_id_identify in(:depId) ")
public class SysUserEntity  implements Serializable{
    //id
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    @Column(name ="ID",nullable=false,length=36)
    private String  id;
    //部门
    @ManyToOne()
    @JoinColumn(name ="dep_id",referencedColumnName="id")
    private SysDepEntity dep;
    //用户名
    @Column(name="username" ,length = 50  ,nullable = true)
    private String  username;
    //密码
    @Column(name="passwd" ,length = 50  ,nullable = true)
    private String  passwd;
    //昵称
    @Column(name="realname" ,length = 50  ,nullable = true)
    private String  realname;
    //用户秘钥
    @Column(name="userkey" ,length = 50  ,nullable = true)
    private String  userkey;
    //状态
    @Column(name="state" ,length = 2  ,nullable = false)
    private Integer  state;
    //是否单一登录
    @Column(name="singlelogin" ,length = 2  ,nullable = true)
    private Integer  singlelogin;
    //是否允许第三方登录
    @Column(name="thirdlogin" ,length = 2  ,nullable = true)
    private Integer  thirdlogin;
    @Column(name="dep_id_identify")
    private Long depIdIdentify;

    //电子签章 签章id
    @Column(name="seal_id")
    private String seal_id;
    //电子签章 签章验证id
    @Column(name="seal_login_id")
    private String seal_login_id;
    //电子签章 签章签名
    @Column(name="seal_name")
    private String seal_name;
    //电子签章 签章部门
    @Column(name="seal_dep")
    private String seal_dep;

    /**
     * 賬號類型
     */
    @Column(name = "user_type")
    private Integer userType;

    public SysUserEntity(){

    }

    public SysUserEntity(String id){
    this.id=id;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public String getPasswd(){
        return passwd;
    }
    public void setPasswd(String passwd){
        this.passwd=passwd;
    }
    public String getRealname(){
        return realname;
    }
    public void setRealname(String realname){
        this.realname=realname;
    }
    public String getUserkey(){
        return userkey;
    }
    public void setUserkey(String userkey){
        this.userkey=userkey;
    }
    public Integer getState(){
        return state;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getSinglelogin(){
        return singlelogin;
    }
    public void setSinglelogin(Integer singlelogin){
        this.singlelogin=singlelogin;
    }
    public Integer getThirdlogin(){
        return thirdlogin;
    }
    public void setThirdlogin(Integer thirdlogin){
        this.thirdlogin=thirdlogin;
    }

    public SysDepEntity getDep() {
        return dep;
    }

    public void setDep(SysDepEntity dep) {
        this.dep = dep;
    }

    public Long getDepIdIdentify() {
        return depIdIdentify;
    }

    public void setDepIdIdentify(Long depIdIdentify) {
        this.depIdIdentify = depIdIdentify;
    }

    public String getSeal_name() {
        return seal_name;
    }

    public void setSeal_name(String seal_name) {
        this.seal_name = seal_name;
    }

    public String getSeal_dep() {
        return seal_dep;
    }

    public void setSeal_dep(String seal_dep) {
        this.seal_dep = seal_dep;
    }

    public String getSeal_id() {
        return seal_id;
    }

    public void setSeal_id(String seal_id) {
        this.seal_id = seal_id;
    }

    public String getSeal_login_id() {
        return seal_login_id;
    }

    public void setSeal_login_id(String seal_login_id) {
        this.seal_login_id = seal_login_id;
    }
}
