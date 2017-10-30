package com.github.restart1025.controller;

import com.github.restart1025.entity.Person;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class MainController {

    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    /**
     * 登陆请求
     * @return
     */
    @RequestMapping("/")
    public String main() {
        return "login";
    }
    /**
     * JSP页面请求
     * @param forName
     * @return
     */
    @RequestMapping("/{forName}")
    public String jspForword(@PathVariable String forName) {
        return forName;
    }

    /**
     * 展示文件页面请求
     * @return
     */
    @RequestMapping("/showFile/showFile")
    @RequiresPermissions("showFile")//文件展示
    public String showFile() {
        return "showFile/showFile";
    }
    /**
     * 上传文件页面请求
     * @return
     */
    @RequestMapping("/uploadFile/uploadFile")
    @RequiresPermissions("uploadFile")//文件上传
    public String uploadFile() {
        return "uploadFile/uploadFile";
    }
    /**
     * 首页页面请求
     * @return
     */
    @RequestMapping("/index/pinBoard")
    @RequiresPermissions("pinBoard")//首页
    public String index() {
        return "index/pinBoard";
    }
    /**
     * 角色页面请求
     * @return
     */
    @RequestMapping("/resource/role")
    @RequiresPermissions("rolePage")//角色管理
    public String role() {
        return "resource/role";
    }
    /**
     * 权限页面请求
     * @return
     */
    @RequestMapping("/resource/permission")
    @RequiresPermissions("permissionPage")//权限管理
    public String permission() {
        return "resource/permission";
    }

    /**
     * 退出页面时清除缓存
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value="/logout",method= RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes ){
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

    /**
     * 404找不到页面
     * @param model
     * @return
     */
    @RequestMapping("/404")
    public String unauthorizedRole(Model model){
        logger.info("------没有权限-------");
        return "error/404";
    }

    @RequestMapping(value="/loginForm", method=RequestMethod.POST)
    public String login(@Valid Person person, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes){

        if( bindingResult.hasErrors() )
            return "login";

        String personId = person.getPersonId();
        UsernamePasswordToken token = new UsernamePasswordToken(person.getPersonId(), person.getPassword());

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();

        try{
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户[" + personId + "]进行登录验证..验证开始");
            currentUser.login(token);
            logger.info("对用户[" + personId + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            logger.info("对用户[" + personId + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        }catch(IncorrectCredentialsException ice){
            logger.info("对用户[" + personId + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        }catch(LockedAccountException lae){
            logger.info("对用户[" + personId + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            logger.info("对用户[" + personId + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.info("对用户[" + personId + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }

        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            logger.info("用户[" + personId + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return "redirect:/main";
        }else{
            token.clear();
            return "redirect:/login";
        }
    }

}
