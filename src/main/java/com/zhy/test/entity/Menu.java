package com.zhy.test.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 主键生成策略
 * JPA提供的四种标准用法为TABLE，SEQUENCE，IDENTITY，AUTO。
 * a，TABLE：使用一个特定的数据库表格来保存主键。
 * b，SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
 * c，IDENTITY：主键由数据库自动生成（主要是自动增长型）
 * d，AUTO：主键由程序控制。
 */
@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_code")
    private String menuCode;
    @Column(name = "url")
    private String url;

    /**
     * fetch = FetchType.EAGER：加载树形结构，使用关闭懒加载。否则取出一层数据后，会关闭session,再取下一层时报错：session is closed
     * @JoinColumn声明的关联关系中：name当前字段。referencedColumnName引用表对应的字段，如果不注明，默认就是引用表的主键。
     *  joinColumns是主操作表的中间列表
     *  inverJoinColumns是从操作表的中间列表。
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_menu",
            joinColumns = @JoinColumn(name = "menu_code",referencedColumnName = "menu_code",updatable = false,insertable = false),
            inverseJoinColumns = @JoinColumn(name = "role_code",updatable = false,insertable = false))
    private List<Role> roleList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
