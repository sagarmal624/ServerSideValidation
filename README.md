# ServerSideValidation
https://www.sagarandcompany.com/

Following example shows how to use Spring native validation in a MVC application. We need to implement Validator interface and perform validation programmatically.
Here is the validator

```java

public class UserValidator implements Validator {
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w\\d._-]+@[\\w\\d.-]+\\.[\\w\\d]{2,6}$");

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == User.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "user.name.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "user.password.empty");
        ValidationUtils.rejectIfEmpty(errors, "emailAddress", "user.email.empty");
        ValidationUtils.rejectIfEmpty(errors, "dateFrom", "user.dateFrom.empty");
        ValidationUtils.rejectIfEmpty(errors, "dateTo", "user.dateTo.empty");

        User user = (User) target;
        if (user.getName() != null && user.getName().length() < 5 ||
                user.getName().length() > 20) {
            errors.rejectValue("name", "user.name.size");
        }

        if (user.getPassword() != null && user.getPassword().contains(" ")) {
            errors.rejectValue("password", "user.password.space");
        }
        if (user.getDateFrom() != null && user.getDateTo() != null) {
            int date = user.getDateFrom().compareTo(user.getDateTo());
            if (date > 0) {
                errors.rejectValue("dateFrom", "user.dateFrom.compare");
            }
        }
        if (user.getPassword() != null && user.getPassword().length() < 5 &&
                user.getPassword().length() > 15) {
            errors.rejectValue("password", "user.password.size");
        }

        if (user.getEmailAddress() != null && !EMAIL_REGEX.matcher(user.getEmailAddress()).matches()) {
            errors.rejectValue("emailAddress", "user.email.invalid");
        }
    }
}

```
#Message Source
src/main/resources/ValidationMessages_en.properties
```java
user.name.empty=User Name cannot be empty.
user.name.size=User Name must be of more than 5 and less than 20 characters.
user.password.empty=User Password cannot be empty.
user.password.size=User Password length must of between 6 and 15.
user.password.space=Password must not have spaces.
user.email.empty=User Email cannot be empty.
user.email.invalid=Email is not valid.
user.dateFrom.compare=From Date shoud be less than to date
user.dateFrom.empty=from date can't be empty
user.dateTo.empty=To date can't be empty

```

#Controller
```java

@RestController
public class UserController {
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public Object handlePostRequest(@ModelAttribute("user") User user,
                                    BindingResult bindingResult, Model model) {
        new UserValidator().validate(user, bindingResult);
        Map map = new HashMap();
        if (bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    String message = messageSource.getMessage(fieldError, null);
                    map.put(fieldError.getField(), message);
                }
            }
            return map;
        }

        return user;
    }

}

```
