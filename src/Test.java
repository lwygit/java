import java.io.*;
import java.util.List;
import java.util.Scanner;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Test {
    public static void main(String[] args) throws IOException, DocumentException {

        Scanner input = new Scanner(System.in);
        boolean isEnd = false;
        System.out.println("----欢迎使用用户管理系统----");

        do {
            System.out.println("————————————————————————————————");
            System.out.println("| 1、登录  2、注册  3、退出系统  |");
            System.out.println("————————————————————————————————");
            System.out.println("请选择：");
            String s1, name, password;
            Test test = new Test();
            s1 = input.nextLine();
            switch (s1) {
                case "1":
                    System.out.print("请输入账号：");
                    name = input.nextLine();
                    System.out.print("请输入密码：");
                    password = input.nextLine();
                    if (test.verifyUserInfoByNamePassword(name, password)) {
                        System.out.println("用户登录成功！");
                        do {
                            User user = new User();

                            System.out.println("————————————————————————————————————————————————————————————————————————————————————————");
                            System.out.println("| 1、查看所有用户  2、查看单个用户  3、新增用户  4、修改用户信息  5、删除用户  6、退出系统  |");
                            System.out.println("————————————————————————————————————————————————————————————————————————————————————————");
                            System.out.println("请选择：");
                            String id;
                            s1 = input.nextLine();
                            switch (s1) {
                                case "1":
                                    //1、查看所有用户
                                    test.showAllUser();
                                    break;
                                case "2":
                                    //2、查看单个用户
                                    System.out.print("请输入用户id：");
                                    test.showUserInfo(input.nextLine());
                                    break;
                                case "3":
                                    //3、新增用户
                                    test.addUser();
                                    break;
                                case "4":
                                    //4、修改用户信息
                                    System.out.print("请输入需要更新的id：");
                                    id = input.nextLine();
                                    test.updateUserInfo(id);
                                    break;
                                case "5":
                                    //5、删除用户
                                    System.out.print("请输入需要删除的用户id：");
                                    id = input.nextLine();
//                                    if (!test.verifyIdExists(id)){
//                                        do {
//                                            System.out.print("id不存在，请重新输入要删除的id：");
//                                            id = input.nextLine();
//                                        }while (test.verifyEmailExists(id));
//                                    }
                                    test.deleteUserInfo(id);
                                    break;
                                case "6":
                                    //6、退出系统
                                    isEnd = true;
                                    break;
                            }
                        }
                        while (!(isEnd));
                        System.out.println("程序结束，欢迎下次使用！");
                        System.exit(0);
                    } else {
                        System.out.println("账号或密码不正确！");
                    }
                    break;
                case "2":
                    //注册
                    test.addUser();
                    break;
                case "3":
                    //退出系统
                    isEnd = true;
                    break;
            }
        }
        while (!(isEnd));
        System.out.println("程序结束，欢迎下次使用！");


    }

    //获取所有用户信息
    public void showAllUser() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> users = root.elements("user");
        for (Element user : users) {
            User user1 = new User();
            user1.id = user.attributeValue("id");
            user1.name = user.elementText("name");
            user1.password = user.elementText("password");
            user1.email = user.elementText("email");
            System.out.println("id：" + user1.id + " " + "账号：" + user1.name + " " + "密码：" + user1.password + " " + "email：" + user1.email);
        }
    }


    //获取单个用户信息
    public void showUserInfo(String id) throws DocumentException {
        SAXReader reader = new SAXReader();
        User user1 = new User();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        int i = 0;

        for (Element user : Users) {
            if (user.attributeValue("id").equals(id)) {
                user1.setId(id);
                user1.setName(user.elementText("name"));
                user1.setPassword(user.elementText("password"));
                user1.setEmail(user.elementText("email"));
                System.out.println("id：" + user1.id + " " + "账号：" + user1.name + " " + "密码：" + user1.password + " " + "email：" + user1.email);
                i++;
            }
        }
        if (i == 0) {
            System.out.println("用户不存在! 查询失败！");
        }
    }

    //新增用户
    public void addUser() throws DocumentException, IOException {
        Scanner input = new Scanner(System.in);
        Test test = new Test();
        User user = new User();
        System.out.print("请输入账号：");
        user.setName(input.nextLine());
        if (test.verifyUsernameExists(user.name)) {
            do {
                System.out.print("账号已存在，请重新输入账号：");
                user.setName(input.nextLine());
            } while (test.verifyUsernameExists(user.name));
        }

        System.out.print("请输入密码：");
        user.setPassword(input.nextLine());
        System.out.print("请输入邮箱：");
        user.setEmail(input.nextLine());
        if (test.verifyEmailExists(user.email)) {
            do {
                System.out.print("邮箱已存在，请重新输入邮箱：");
                user.setEmail(input.nextLine());
            } while (test.verifyEmailExists(user.email));
        }
        test.insertUserInfo(user);
    }


    //验证id是否存在  true存在  false不存在
    public boolean verifyIdExists(String id) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        boolean b = false;
        for (Element user : Users) {
            if (user.attributeValue("id").equals(id)) {
                b = true;
                break;
            }
        }
        return b;
    }

    //验证用户名是否占用  true存在  false不存在
    public boolean verifyUsernameExists(String name) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        boolean b = false;
        for (Element user : Users) {
            if (user.elementText("name").equals(name)) {
                b = true;
                break;
            }
        }
        return b;
    }

    //验证email是否存在  true存在  false不存在
    public boolean verifyEmailExists(String email) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        boolean b = false;
        for (Element user : Users) {
            if (user.elementText("email").equals(email)) {
                b = true;
                break;
            }
        }
        return b;
    }

    //验证用户信息
    public boolean verifyUserInfoByNamePassword(String name, String password) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        boolean b = false;
        for (Element user : Users) {
            if (user.elementText("name").equals(name) && user.elementText("password").equals(password)) {
                b = true;
                break;
            }
        }
        return b;
    }

    //修改用户信息
    public void updateUserInfo(String id) throws DocumentException, IOException {
        Scanner input = new Scanner(System.in);
        Test test = new Test();
        SAXReader reader = new SAXReader();
        int i = 0;
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        for (Element user : Users) {
            if (user.attributeValue("id").equals(id)) {
                System.out.print("请输入账号：");
                String name = input.nextLine();
                System.out.print("请输入密码：");
                String password = input.nextLine();
                System.out.print("请输入邮箱：");
                String email = input.nextLine();

                user.element("name").setText(name);
                user.element("password").setText(password);
                user.element("email").setText(email);

                save(doc);

                System.out.println("更新成功！" + "id：" + user.attributeValue("id") + " " + "账号：" + user.elementText("name") + " "
                        + "密码：" + user.elementText("password") + " " + "email：" + user.elementText("email"));
                i++;
            }
        }
        if (i == 0) {
            System.out.println("用户不存在！ 修改失败！");
        }
    }


    //新增用户
    public void insertUserInfo(User user) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();


        String id = "0";
        List<Element> Users = root.elements("user");
        for (Element u : Users) {
            id = u.attributeValue("id");
        }
        Integer i = Integer.parseInt(id) + 1;

        Element newUser = root.addElement("user");
        newUser.addAttribute("id", i.toString());
        newUser.addElement("name").setText(user.getName());
        newUser.addElement("password").setText(user.getPassword());
        newUser.addElement("email").setText(user.getEmail());
        System.out.println("添加用户成功！");
        System.out.println("id：" + newUser.attributeValue("id") + " " + "账号：" + newUser.elementText("name") + " "
                + "密码：" + newUser.elementText("password") + " " + "email：" + newUser.elementText("email"));

        save(doc);

    }

    //删除单个用户信息
    void deleteUserInfo(String id) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        boolean b = false;
        Document doc = reader.read(new File("XMLTest.xml"));
        Element root = doc.getRootElement();
        List<Element> Users = root.elements("user");
        for (Element user : Users) {
            if (user.attributeValue("id").equals(id)) {
                root.remove(user);
                b = true;
                System.out.println("用户删除成功！");
            }
        }
        if (b==false) {
            System.out.println("用户不存在！ 删除失败！");
        }

        save(doc);
    }


    public static class User {
        String id;
        String name;
        String password;
        String email;

        void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        void setName(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void setEmail(String email) {
            this.email = email;
        }

        String getEmail() {
            return email;
        }

        void setPassword(String password) {
            this.password = password;
        }

        String getPassword() {
            return password;
        }
    }

    void save(Document doc) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream("XMLTest.xml"), format);
        xmlWriter.write(doc);
        xmlWriter.close();

    }

}