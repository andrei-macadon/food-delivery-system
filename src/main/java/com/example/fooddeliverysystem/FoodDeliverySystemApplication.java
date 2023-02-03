package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.CityRepo;
import com.example.fooddeliverysystem.repo.FoodCategoryRepo;
import com.example.fooddeliverysystem.repo.MenuItemRepo;
import com.example.fooddeliverysystem.repo.RestaurantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class FoodDeliverySystemApplication {

    private final CityRepo cityRepo;
    private final RestaurantRepo restaurantRepo;
    private final FoodCategoryRepo foodCategoryRepo;
    private final MenuItemRepo menuItemRepo;

    @Autowired
    public FoodDeliverySystemApplication(CityRepo cityRepo, RestaurantRepo restaurantRepo,
                                         FoodCategoryRepo foodCategoryRepo, MenuItemRepo menuItemRepo) {
        this.cityRepo = cityRepo;
        this.restaurantRepo = restaurantRepo;
        this.foodCategoryRepo = foodCategoryRepo;
        this.menuItemRepo = menuItemRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(FoodDeliverySystemApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run() {
//        return args -> {
//
//            if (!restaurantRepo.existsByName("Stadio")) {
//
//                // PASTA
//                MenuItem PastaTajarinAlTartufo = MenuItem.builder()
//                        .ingredients("Tajarin is the Piemontese version of tagliatelle. Unlike other types of egg pasta," +
//                                " these thin, golden strands are made with a higher proportion of egg yolks," +
//                                " resulting in a delicate texture and rich flavor." +
//                                " Paired with the prized white truffle, they make a decadent primo piatto.")
//                        .name("Tajarin Al Tartufo")
//                        .price(BigDecimal.valueOf(67))
//                        .timeToCook(BigDecimal.valueOf(25))
//                        .build();
//
//                MenuItem PastaVesuvioalRagùdiSalsiccia = MenuItem.builder()
//                        .ingredients("Vesuvio is a short pasta named for the famous volcano of the same name in Campania. " +
//                                "The twists and turns of this short pasta make it perfect for catching the chunky bits of" +
//                                " tomato and sausage in this Neapolitan-style ragù.")
//                        .name("Vesuvio al Ragùdi Salsiccia")
//                        .price(BigDecimal.valueOf(56))
//                        .timeToCook(BigDecimal.valueOf(20))
//                        .build();
//
//                MenuItem PastaRaviolidiZucca = MenuItem.builder()
//                        .ingredients("Sweet butternut squash ravioli, nutty browned butter, and fragrant sage are a classic," +
//                                " consistently delicious pairing, not to mention the ultimate Fall meal. " +
//                                "The secret ingredient? Crushed amaretti cookies give the filling extra depth. " +
//                                "Bonus: it will make your kitchen smell amazing!")
//                        .name("Ravioli di Zucca")
//                        .price(BigDecimal.valueOf(62))
//                        .timeToCook(BigDecimal.valueOf(20))
//                        .build();
//
//                MenuItem PastaPaccherialForno = MenuItem.builder()
//                        .ingredients("Typical of Calabria, this recipe for paccheri al forno pulls together quickly and relies" +
//                                " on the oven to do most of the work. Look for high-quality cherry tomatoes and fresh mozzarella," +
//                                " plus bronze-extruded, air-dried pasta, whose coarse texture will hold up to the sauce.")
//                        .name("Paccheria al Forno")
//                        .price(BigDecimal.valueOf(45))
//                        .timeToCook(BigDecimal.valueOf(22))
//                        .build();
//
//                MenuItem PastaallaCarbonara = MenuItem.builder()
//                        .ingredients("The recipe for Pasta alla Carbonara has humble roots in the Apennine hills of central " +
//                                "Italy, not far from Roma. The dish was known as the shepherds’ favorite as they " +
//                                "roamed the hilly pastures following the movement of flocks, a practice known as " +
//                                "transumanza, thanks to its simple, readily available ingredients: egg, guanciale, and cheese.")
//                        .name("Pasta alla Carbonara")
//                        .price(BigDecimal.valueOf(44))
//                        .timeToCook(BigDecimal.valueOf(18))
//                        .build();
//
//                MenuItem PastaBucatiniallAmatriciana = MenuItem.builder()
//                        .ingredients("Named for the town of Amatrice, located about an hour northeast of Roma, this simple " +
//                                "dish combines sweet and tangy tomato sauce with rich guanciale (cured pork jowl) and sharp " +
//                                "Pecorino Romano DOP cheese, with a spicy kick from peperoncini, or dried chili flakes. " +
//                                "The best part? The hollow bucatini make each bite extra saucy.")
//                        .name("Bucatini all'Ammatraciana")
//                        .price(BigDecimal.valueOf(48))
//                        .timeToCook(BigDecimal.valueOf(24))
//                        .build();
//
//                MenuItem PastaalTonno = MenuItem.builder()
//                        .ingredients("A coastal classic with origins in Calabria, pasta with tuna is a simple yet satisfying" +
//                                " main dish that works as well for a quick weeknight dinner as it does for a lazy Sunday feast." +
//                                " With the addition of salty capers and spicy Calabrian chili peppers, this plate has the power" +
//                                " to transport you to the Mediterranean seaside.")
//                        .name("Pasta al Tonno")
//                        .price(BigDecimal.valueOf(43))
//                        .timeToCook(BigDecimal.valueOf(20))
//                        .build();
//
//                MenuItem PastaSpaghettiCacioePepe = MenuItem.builder()
//                        .ingredients("Rich cheese, bronze-extruded pasta, and freshly-ground pepper — the ultimate comfort food," +
//                                "cacio e pepe is a Roman dish that will make you forget all about mac-and-cheese. " +
//                                "This dish is simple on paper, but can be difficult to master at home; high-quality ingredients" +
//                                " and patience will help you achieve the optimum cremina, or cheese sauce.")
//                        .name("Spaghetti Cacio e Pepe")
//                        .price(BigDecimal.valueOf(40))
//                        .timeToCook(BigDecimal.valueOf(17))
//                        .build();
//
//
//                // BURGERS
//
//                MenuItem BurgerAuCheval = MenuItem.builder()
//                        .ingredients("According to Bill Addison, this haute Chicago diner burger, which boasts two beef " +
//                                "patties on the \"single\" hamburger, represents \"one of the country's best examples of " +
//                                "the flattop-griddled burger hoisted to haute levels without losing sight of its diner roots.\"" +
//                                " American cheese slices and a Dijon/lemon/mayo-based sauce adds depth of flavor, and \"the " +
//                                "kitchen nails the ratios: It's impressive how well the bun cradles all those calories without" +
//                                " meddling with the meaty savor.\" Addison adds one pro tip: \"I know some people relish the " +
//                                "power move of adding foie, but after trying it with and without the liver I don't think this " +
//                                "burger needs any more embellishment.\"")
//                        .name("Au Cheval")
//                        .price(BigDecimal.valueOf(56))
//                        .timeToCook(BigDecimal.valueOf(45))
//                        .build();
//
//                MenuItem BurgerCraigieOnMain = MenuItem.builder()
//                        .ingredients("Alongside Michael Schlow's well-loved Radius burger, Eater Boston editor Rachel " +
//                                "Blumenthal calls the Craigie on Main burger a \"pioneer of Boston's 'upscale' burger class.\" " +
//                                "Chef Tony Maws' now-iconic version features a house-made milk-style bun, Maws' mace ketchup," +
//                                " vinaigrette-dressed greens, and a patty highlighting \"impeccable ingredients,\" Blumenthal" +
//                                " says. As for how it tastes? \"From the giant, juicy patty to the bun that achieves that perfect " +
//                                "texture between soft and firm, it's one of the best burgers you'll eat.\"")
//                        .name("CraigieOnMain")
//                        .price(BigDecimal.valueOf(52))
//                        .timeToCook(BigDecimal.valueOf(40))
//                        .build();
//
//                MenuItem BurgerTheSpottedPig = MenuItem.builder()
//                        .ingredients("This is a burger with a Michelin star, the work of chef April Bloomfield at her 11-year-old " +
//                                "Greenwich Village gastropub the Spotted Pig. \"You might not think you're a roquefort-topped" +
//                                " burger person, but the Spotted Pig will make you one,\" says Eater associate reports editor" +
//                                " Hillary Dixler. Bloomfield serves her burgers atop an ever-so-slightly sweet toasted brioche bun," +
//                                " and Dixler offers a pro tip: \"Don't forget to ask for some dijon mustard: just a tiny bit cuts" +
//                                " through those rich flavors and takes the thing home.\" ")
//                        .name("CraigieOnMain")
//                        .price(BigDecimal.valueOf(45))
//                        .timeToCook(BigDecimal.valueOf(30))
//                        .build();
//
//                MenuItem BurgerHusk = MenuItem.builder()
//                        .ingredients("This is basically God on a squishy bun,\" Addison says of chef Sean Brock's now-famous burger," +
//                                " which exists in slightly modified versions at his Charleston and Nashville Husk outposts." +
//                                " The nod here goes to the original beast, featuring hickory-smoked Benton's bacon ground directly" +
//                                " into the chuck blend. \"By grinding bacon with the ground beef the burger takes on the quality " +
//                                "similar to Southern vegetables long simmered with pork,\" Addison says.  ")
//                        .name("Husk")
//                        .price(BigDecimal.valueOf(50))
//                        .timeToCook(BigDecimal.valueOf(30))
//                        .build();
//
//                MenuItem BurgerLePigeon = MenuItem.builder()
//                        .ingredients("Before chef Gabriel Rucker's James Beard Award for Rising Star Chef skewed the menu more" +
//                                " toward haute tastings, his iconic Portland restaurant Le Pigeon helped usher in the era of the" +
//                                " rules-breaking bistro burger. In earlier incarnations, Rucker — who would limit the burger to a" +
//                                " scant five per evening — reached for unconventional items like sharp white cheddar, grilled picked onions," +
//                                " and a ciabatta roll, to the chagrin of burger purists. These days, the recipe remains the same," +
//                                " but the burger is available in unlimited quantities.")
//                        .name("Le Pigeon")
//                        .price(BigDecimal.valueOf(60))
//                        .timeToCook(BigDecimal.valueOf(36))
//                        .build();
//
//                MenuItem BurgerNopa = MenuItem.builder()
//                        .ingredients("A more modern version of the burger in the Zuni Cafe mold (read: California seasonal)," +
//                                " Nopa's burger \"has become an Americana beacon in a city known for its obsession with produce" +
//                                " and seasonality,\" Addison says. According to Eater SF editor Allie Pape, the grass-fed beef" +
//                                " patty is aided by a secret weapon: \"It's grilled over an almond-wood fire, which makes it" +
//                                " deliciously fragrant and smoky.\" Addison suggests diners add a \"smattering\" of blue cheese" +
//                                " on top for the ultimate experience: \"You can come to the restaurant and eat gorgeous, of-the-moment" +
//                                " vegetables, but then you pick up this stunner and feel such primal satisfaction.\"")
//                        .name("Nopa")
//                        .price(BigDecimal.valueOf(68))
//                        .timeToCook(BigDecimal.valueOf(41))
//                        .build();
//
//
//                // PIZZA
//
//                MenuItem PizzaCheese = MenuItem.builder()
//                        .ingredients("It should be no shocker that a classic is the statistical favorite. Cheese pizza is one" +
//                                " of the most popular choices. It will always be a simple, unadorned masterpiece on its own.")
//                        .name("Cheese Pizza")
//                        .price(BigDecimal.valueOf(30))
//                        .timeToCook(BigDecimal.valueOf(15))
//                        .build();
//
//                MenuItem PizzaVeggie = MenuItem.builder()
//                        .ingredients("When you want to jazz up your cheese pizza with color and texture, veggies are the perfect" +
//                                " topping. And you’re only limited by your imagination. Everything from peppers and mushrooms, " +
//                                "to eggplant and onions make for an exciting and tasty veggie pizza.")
//                        .name("Veggie Pizza")
//                        .price(BigDecimal.valueOf(28))
//                        .timeToCook(BigDecimal.valueOf(15))
//                        .build();
//
//                MenuItem PizzaPepperoni = MenuItem.builder()
//                        .ingredients("There’s a reason this is one of the most popular types of pizza. Who doesn’t love biting" +
//                                " into a crispy, salty round of pepperoni?")
//                        .name("Pepperoni Pizza")
//                        .price(BigDecimal.valueOf(32))
//                        .timeToCook(BigDecimal.valueOf(17))
//                        .build();
//
//                MenuItem PizzaMeat = MenuItem.builder()
//                        .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
//                                "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
//                        .name("Meat Pizza")
//                        .price(BigDecimal.valueOf(35))
//                        .timeToCook(BigDecimal.valueOf(20))
//                        .build();
//
//                MenuItem PizzaMargherita = MenuItem.builder()
//                        .ingredients("Deceptively simple, the Margherita pizza is made with basil, fresh mozzarella, and tomatoes. " +
//                                "There’s a reason it’s an Italian staple and one of the most popular types of pizza in the country.")
//                        .name("Margherita Pizza")
//                        .price(BigDecimal.valueOf(20))
//                        .timeToCook(BigDecimal.valueOf(15))
//                        .build();
//
//                MenuItem PizzaBBQChicken = MenuItem.builder()
//                        .ingredients("If you love BBQ chicken and you love pizza, why not put them together? This has long" +
//                                " been a cult favorite of sports fans and college kids. The chicken slathered over the top" +
//                                " of a pie gives it a tangy, sweet flavor that can’t be beaten.")
//                        .name("BBQ Chicken Pizza")
//                        .price(BigDecimal.valueOf(30))
//                        .timeToCook(BigDecimal.valueOf(20))
//                        .build();
//
//
//                FoodCategory foodCategoryPastaRestaurant1 = FoodCategory.builder()
//                        .foodCategoryName("Pasta")
//                        .menuItems(List.of(PastaTajarinAlTartufo, PastaVesuvioalRagùdiSalsiccia, PastaBucatiniallAmatriciana,
//                                PastaPaccherialForno, PastaallaCarbonara))
//                        .build();
//
//                FoodCategory foodCategoryPizzaRestaurant1 = FoodCategory.builder()
//                        .foodCategoryName("Pizza")
//                        .menuItems(List.of(PizzaMargherita, PizzaBBQChicken, PizzaMeat, PizzaPepperoni))
//                        .build();
//
//
//                Restaurant restaurant1 = Restaurant.builder()
//                        .address("Bulevardul Unirii 1")
//                        .name("Stadio")
//                        .foodCategoryList(List.of(foodCategoryPastaRestaurant1, foodCategoryPizzaRestaurant1))
//                        .build();
//
//
//                City bucharestCity = City.builder()
//                        .name("Bucharest")
//                        .zipcode("11821")
//                        .restaurants(List.of(restaurant1))
//                        .build();
//
////                cityRepo.save(bucharestCity);
////
////                restaurant1.setCity(bucharestCity);
////                restaurantRepo.save(restaurant1);
////
////                foodCategoryPastaRestaurant1.setRestaurantMenu(restaurant1);
////                foodCategoryPizzaRestaurant1.setRestaurantMenu(restaurant1);
////                foodCategoryRepo.saveAll(List.of(foodCategoryPastaRestaurant1, foodCategoryPizzaRestaurant1));
////
////                PastaTajarinAlTartufo.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaVesuvioalRagùdiSalsiccia.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaRaviolidiZucca.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaPaccherialForno.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaallaCarbonara.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaBucatiniallAmatriciana.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaalTonno.setFoodCategory(foodCategoryPastaRestaurant1);
////                PastaSpaghettiCacioePepe.setFoodCategory(foodCategoryPastaRestaurant1);
////                PizzaCheese.setFoodCategory(foodCategoryPizzaRestaurant1);
////                PizzaBBQChicken.setFoodCategory(foodCategoryPizzaRestaurant1);
////                PizzaMargherita.setFoodCategory(foodCategoryPizzaRestaurant1);
////                PizzaMeat.setFoodCategory(foodCategoryPizzaRestaurant1);
////                PizzaPepperoni.setFoodCategory(foodCategoryPizzaRestaurant1);
////                PizzaVeggie.setFoodCategory(foodCategoryPizzaRestaurant1);
////                menuItemRepo.saveAll(List.of(PastaTajarinAlTartufo, PastaVesuvioalRagùdiSalsiccia, PastaRaviolidiZucca,
////                        PastaPaccherialForno, PastaallaCarbonara, PastaBucatiniallAmatriciana, PastaalTonno, PastaSpaghettiCacioePepe,
////                        PizzaCheese, PizzaVeggie, PizzaPepperoni, PizzaMeat, PizzaMargherita, PizzaBBQChicken));
//
//            }
//
//        };
//    }
}
