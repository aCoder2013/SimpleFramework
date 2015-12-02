# SimpleFramework
This is a very simple Java Web Framework,here is a demo :
```java
        @Controller("/test")
        @AOP({HelloInterceptor.class,LogInterceptor.class})
        public class HelloServlet{
        
            @Inject
            private HelloService helloService;
        
            @RequestMapping(value ="/hello",method = RequestMethod.GET)
            @AOP(MethodInterceptor.class)
            public ModelAndView hello() {
                ModelAndView modelAndView = new ModelAndView("index.jsp");
                modelAndView.addAttribute("currentTime","Mars");
                return modelAndView;
            }
        
            @RequestMapping(value = "/form",method = RequestMethod.POST)
            public ModelAndView formTest(@RequestParam("name") String name,@RequestParam("img")MultipartFile file
            ,@RequestParam("img1")MultipartFile file1){
                ModelAndView modelAndView = new ModelAndView("index.jsp");
                System.out.println(file);
                System.out.println(file1);
                file.uploadTo(new File("D:/aboutcoding/hahahhaa/"+file.getOriginalFilename()));
                modelAndView.addAttribute("currentTime",name);
                return modelAndView;
            }
        
        
            @IgnoreAop
            @RequestMapping(value = "/user",method = RequestMethod.GET)
            public User user(){
                return helloService.getUser();
            }
        }
```
And the interceptor just needs to implements Interceptor!
```java
        public class HelloInterceptor  implements Interceptor {
        
        
            @Override
            public void before(Method method, Object... args) {
                System.out.println("Hello Interceptor Before");
            }
        
            @Override
            public void after(Method method, Object... args) {
                System.out.println("Hello Interceptor After");
            }
        }

```
