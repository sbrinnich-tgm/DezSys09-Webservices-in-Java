package brinnich.jersey;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//Path: http://localhost/<appln-folder-name>/register
@Path("register")
public class Register {
    // HTTP Get Method
    @GET
    // Path: http://localhost/<appln-folder-name>/register/doregister
    @Path("/doregister")  
    // Produces JSON as response
    @Produces(MediaType.APPLICATION_JSON) 
    // Query parameters are parameters: http://localhost/<appln-folder-name>/register/doregister?name=pqrs&username=abc&password=xyz
    public String doRegister(@QueryParam("name") String name, @QueryParam("username") String uname, @QueryParam("password") String pwd){
        String response = "";
        int retCode = registerUser(name, uname, pwd);
        if(retCode == 0){
            response = Utility.constructJSON("register",true);
        }else if(retCode == 1){
            response = Utility.constructJSON("register",false, "You are already registered");
        }else if(retCode == 2){
            response = Utility.constructJSON("register",false, "Error occured");
        }
        return response;
 
    }
 
    private int registerUser(String name, String uname, String pwd){
        int result = 2;
        if(Utility.isNotNull(uname) && Utility.isNotNull(pwd)){
            try {
                if(DBConnection.insertUser(name, uname, pwd)){
                    result = 0;
                }
            } catch(SQLException sqle){
                //When Primary key violation occurs that means user is already registered
                if(sqle.getMessage().contains("UNIQUE constraint failed")){
                    result = 1;
                }
            }
            catch (Exception e) {
                result = 2;
            }
        }else{
            result = 2;
        }
 
        return result;
    }
 
}
