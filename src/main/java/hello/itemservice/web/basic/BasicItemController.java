package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    /*
        // Required Args Constructor 로 대채
        public BasicItemController(ItemRepository itemRepository) {
            this.itemRepository = itemRepository;
        }
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item(itemName, price, quantity);
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        Item save = itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {
//        ModelAttribute 가 만들어주는 효과
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);

        itemRepository.save(item);

        // @ModelAttribute("item") "item" 이라는 이름으로 넣어줌 생략 가능
//        model.addAttribute("item", item);

        return "basic/item";
    }


    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        // ModelAttribute의 name 생략시 Class명 첫글자 소문자로 바꾸어 ModelAttribute 에 넣어줌
        // Item -> item
        itemRepository.save(item);
        return "basic/item";
    }

    // PRG Post/Redirect/Get 문제
    // 웹 브라우저의 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
    // 계속 새로고침할경우, 같은내용으로 /add 가 반복된다.
    // 따라서 Redirect 로 응답하여 GET 을 마지막 요청에 남기도록 한다.
//    @PostMapping("/add")
    public String addItemV4(Item item) {// ModelAttribute 생략 가능
        itemRepository.save(item);
        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV5(Item item, RedirectAttributes redirectAttributes) {// ModelAttribute 생략 가능
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; // RediectAttribute 의 itemId 치환, status 는 QueryParam(?status=true)로 저장
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * Test Data Add
     */

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
