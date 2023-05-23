package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private String GET="GET", API_POSTS="/api/posts", POST ="POST", DELETE="DELETE", API_POSTS_d="/api/posts/\\d+";


  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.netology");
    controller= context.getBean(PostController.class);
//    final var repository = new PostRepository();
//    final var service = new PostService(repository);
//    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(GET) && path.equals(API_POSTS)) {
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(API_POSTS)) {
        parseLong_Id(path,resp);
        // easy way
        return;
      }
      if (method.equals(POST) && path.equals(API_POSTS)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(API_POSTS_d)) {
        parseLong_Id(path,resp);
        // easy way
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  private void parseLong_Id(String path, HttpServletResponse resp) throws IOException {
    final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
    controller.removeById(id, resp);
  }
}

