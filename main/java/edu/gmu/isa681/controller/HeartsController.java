/**
 * http://websystique.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate/
 * http://websystique.com/spring-security/spring-security-4-custom-login-form-annotation-example/
 * http://websystique.com/springmvc/spring-4-mvc-helloworld-tutorial-full-example/
 http://www.mkyong.com/spring-mvc/spring-4-mvc-ajax-hello-world-example/


 * why to use hibernate?
 * http://software-security.sans.org/developer-how-to/fix-sql-injection-in-java-hibernate



 http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/
 http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/
 https://spring.io/guides/gs/securing-web/#use-sts
 */

package edu.gmu.isa681.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.gmu.isa681.model.Player;
import edu.gmu.isa681.model.UnregisteredPlayer;
import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.PlayerGamesDto;
import edu.gmu.isa681.service.GameService;
import edu.gmu.isa681.service.PlayerService;
import edu.gmu.isa681.service.UnregisteredPlayerService;

@Controller
public class HeartsController {

	@Autowired
	PlayerService playerService;

	@Autowired
	UnregisteredPlayerService unregisteredPlayerService;
	
	@Autowired
	GameService gameService;

    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        return "welcome";
    }
 
    /*
     * 13-Nov-2015 17:39:05.211 WARNING [http-nio-8010-exec-2] org.springframework.web.servlet.PageNotFound.handleHttpRequestMethodNotSupported Request method 'POST' not supported
13-Nov-2015 17:39:05.215 WARNING [http-nio-8010-exec-2] org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver.logException Handler execution resulted in exception: Request method 'POST' not supported

, method = RequestMethod.POST)
    public @ResponseBody
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHome(ModelMap model) {
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
        
    	PlayerGamesDto playerGamesDto = gameService.getPlayerGamesResults(playerId);
        model.addAttribute("loggedInPlayerName", LoggedInPlayer.getLoggedInPlayerName());
        if(playerGamesDto != null) {
	        model.addAttribute("playerWins", playerGamesDto.getTotalWins());
	        model.addAttribute("playerLosses", playerGamesDto.getTotalLosses());
	        model.addAttribute("playersScores", playerGamesDto.getGames());
        }
        
        return "home";
    }
    
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String newRegistration(ModelMap model) {
    	UnregisteredPlayer uPlayer = new UnregisteredPlayer();
        model.addAttribute("player", uPlayer);
        return "register";
    }

    /*
     * This method will be called on form submission, handling POST request It
     * also validates the player input
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegistration(@Valid UnregisteredPlayer uPlayer,
            BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            System.out.println("There are errors");
            return "register";
        }
        
        Player player = new Player();
        String errMsg = "";
        
        if (!uPlayer.getPassword().equals(uPlayer.getPassword2())) {
        	errMsg = "Passwords do not match";
        } if (unregisteredPlayerService.isValidPlayerFirstName(uPlayer.getFirstName()) == 0) {
        	errMsg = "Invalid First Name.";
        } else if (unregisteredPlayerService.isValidPlayerLastName(uPlayer.getLastName()) == 0) {
        	errMsg = "Invalid Last Name.";
        } else if (unregisteredPlayerService.isValidPlayerSso(uPlayer.getSsoId()) == 0) {
        	errMsg = "Invalid SSO ID.";
        } else if (unregisteredPlayerService.isValidPlayerPassword(uPlayer.getPassword(), uPlayer.getPassword2()) == 0) {
        	errMsg = "Invalid Password.";
        } else if (unregisteredPlayerService.isValidPlayerEmail(uPlayer.getEmail()) == 0) {
        	errMsg = "Invalid Email Address.";
        }
        
        if (!errMsg.isEmpty()) {
            model.addAttribute("failure", errMsg);
            model.addAttribute("playerError", "Y");
            model.addAttribute("player", uPlayer);
        	return "register";
        }
        
        player.setFirstName(uPlayer.getFirstName());
        player.setLastName(uPlayer.getLastName());
        player.setSsoId(uPlayer.getSsoId());
        player.setPassword(uPlayer.getPassword());
        player.setEmail(uPlayer.getEmail());

        System.out.println("First Name : "+player.getFirstName());
        System.out.println("Last Name : "+player.getLastName());
        System.out.println("SSO ID : "+player.getSsoId());
        System.out.println("Password : "+player.getPassword());
        System.out.println("Email : "+player.getEmail());
        System.out.println("Checking UsrProfiles....");

        playerService.save(player);
        model.addAttribute("success", "Player " + player.getFirstName() + " has been registered successfully");
        model.addAttribute("newPlayer", "Y");
        return "login";
    }

    @RequestMapping(value="/board", method = RequestMethod.GET)
    public String board(ModelMap model) {
    	
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	
    	// 1. a) Check if the player already in a game and the game is not over, then return the game Id.
    	//    b) Else check if there is an open game then have him join the game and return the game Id.
    	
    	GameDto game = gameService.joinAGame(playerId);
    	System.out.println("++++++++++++++++++++++++" + game.getPlayerId());
        model.addAttribute("loggedInPlayerName", LoggedInPlayer.getLoggedInPlayerName());
        model.addAttribute("loggedInPlayerId", LoggedInPlayer.getLoggedInPlayerId());
        // note: we have 4, so four minus how many already signed up for the game
        //       we also add one to count the current player ...
        int numOfPlayersInGame = game.getOpponents().size() + 1; // 1 for the current player
    	System.out.println("++++++++++++++++++++++++ numOfPlayersInGame = " + numOfPlayersInGame);
        int quorum = (4 - numOfPlayersInGame) == 0 ? 1 : 0;

        model.addAttribute("oppSize", 4 - (game.getOpponents().size() + 1)); 
        model.addAttribute("game", game);
        model.addAttribute("quorum", quorum);

        return "board";
    }

    @RequestMapping(value="/play", method = RequestMethod.GET)
    public String play(@RequestParam(value="gameId", required=true) int gameId, 
    		           @RequestParam(value="cardId", required=true) String cardId, 
    		           ModelMap model) {
    	
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	
    	// First mark the card that it has been used
    	System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
    	System.out.println("+++++++++++++++++++++++++ gameId = " + gameId);
    	System.out.println("+++++++++++++++++++++++++ cardId = " + cardId);
    	cardId = cardId.replaceAll("'", "");
    	System.out.println("+++++++++++++++++++++++++ cardId = " + cardId);
    	gameService.play(playerId, gameId, cardId);
    	
    	// 1. a) Check if the player already in a game and the game is not over, then return the game Id.
    	//    b) Else check if there is an open game then have him join the game and return the game Id.
    	
    	GameDto gameDto = gameService.joinAGame(playerId);
    	System.out.println("++++++++++++++++++++++++" + gameDto.getPlayerId());
        model.addAttribute("loggedInPlayer", LoggedInPlayer.getLoggedInPlayerName());
        model.addAttribute("loggedPlayerId", LoggedInPlayer.getLoggedInPlayerId());
        // note: we have 4, so four minus how many already signed up for the game
        //       we also add one to count the current player ...
        int numOfPlayersInGame = gameDto.getOpponents().size() + 1; // 1 for the current player
    	System.out.println("++++++++++++++++++++++++ numOfPlayersInGame = " + numOfPlayersInGame);
        int quorum = (4 - numOfPlayersInGame) == 0 ? 1 : 0;

        model.addAttribute("oppSize", 4 - (gameDto.getOpponents().size() + 1)); 
        model.addAttribute("game", gameDto);
        model.addAttribute("quorum", quorum);

        return "board";
    }
}
