# Vision #
The vision of Appify Framework is to provide a full-stack framework to build scalable high-performance business applications with minimal effort. Appify Framework targets a large range of server and client platforms. Appify Framework allows the developer to leverage his/her development skills to focus on the business problem.

# Code style #
This is an example of the code style pursued by Appify Framework. Focus is on readability, lack of boilerplate and reusability. You could add a single item to the cart by calling `cart.add(item)` or `cart.add(item).times(1)`. You can also add the same item multiple times: `cart.add(item).times(3)`.

```
public class Item {
  private static final BinaryProcedure<Cart, Item> ADD = new AddToCartProcedure();

  private String name;
  private Image image;

  // Simple constructor and getters

  // Find the cart in the session scope
  public RepeatableProcedure addToCart(@Session final Cart cart) {
    return new RunAtLeastOnceRepeatableProcedure() {
      @Override
      public void run() {
        ADD.run(cart, Item.this);
      }
  }
}

public class AddToCartProcedure implements BinaryProcedure<Cart, Item> {
  @Override
  public void run(Cart cart, Item item) {
    cart.add(item);
  }
}

public abstract class RunAtLeastOnceRepeatableProcedure implements RepeatableProcedure {
  public RunAtLeastOnceRepeatableProcedure() {
    run();
  }

  // Expose number of runs as a parameter
  @Override
  public void times(@Parameter int numberOfRuns) {
    if(numberOfRuns < 1) {
      throw new IllegalArgumentException("Cannot run less than once");
    }
    int i = 0;
    // we ran once already, so subtract one
    while(i < numberOfRuns - 1) {
      i++;
      run();
    }
  }
}
```

Add a simple HTML template and [Twitter Bootstrap](http://twitter.github.com/bootstrap/):

```
<div class="span4">
    <h3>${item.name}</h3>
    <p><img src="${item.image.url}" style="height: 250px"/></p>
    <div class="row">
        <div class="span1">
            <p class="price">${item.price}</p>
        </div>
        <div class="span3">
            <form data-method="${item.addToCart}" data-view="inline">
            </form>
        </div>
    </div>
</div>
```

Then this is the result:

![http://appify-framework.googlecode.com/svn/wiki/shop.png](http://appify-framework.googlecode.com/svn/wiki/shop.png)

# Target platforms #

## Server platforms ##
  * Google App Engine
  * Tomcat 7+
  * Embedded (executable jar)
  * Java EE 6 Web Profile-compliant application server
  * Cloud Foundry

## Client platforms ##
  * Chrome
  * Firefox
  * IE 7+
  * Safari
  * Opera

## Mobile platforms ##
  * Android
  * iOS
  * Windows Phone 7
  * Blackberry

