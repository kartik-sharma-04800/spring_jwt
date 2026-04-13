export default {
  async fetch(request) {
    const url = new URL(request.url);

    const target = "https://spring-jwt-sgbc.onrender.com" + url.pathname;

    return fetch(target, {
      method: request.method,
      headers: request.headers,
      body: request.body
    });
  }
};