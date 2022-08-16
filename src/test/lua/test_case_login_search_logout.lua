local step = "login";
credentials = {"username=godhappy&password=qwe1234", "username=anna_kp&password=qwe1234"}
search_params = {"firstNamePattern=Wing&lastNamePattern=Huff&limit=50", "firstNamePattern=d&lastNamePattern=l&limit=100", "firstNamePattern=a&lastNamePattern=b&limit=10"}

logfile = io.open("wrk.log", "w");

request_login = function()
    local headers = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";
    local body = credentials[math.random(#credentials)];

    debug("Try to login with: " .. body .. "\n");

    return wrk.format("POST", "/login", headers, body);
end

request_search = function()
    local headers = {}
    headers["Content-Type"] = "application/x-www-form-urlencoded"
    if cookie then
        headers["Cookie"] = cookie
    end
    local body = search_params[math.random(#search_params)]
    debug("Search with: " .. body .. "\n");
    return wrk.format("POST", "/search", headers, body)
end

request_logout = function()
    return wrk.format("GET", "/logout", nil, nil)
end

request = function()
    if step == "login" then
	    return request_login();
    elseif step == "search" then
	    return request_search();
    elseif step == "logout" then
        return request_logout();
    end;
    return request_login();
end

response = function(status, headers, body)
    debug("Response on step: " .. step .. ":")
    if step == "login" then
        if status == 302 then
            step = "search"
            debug("success\n");
    	    for k,v in pairs(headers) do
                if string.starts(k, "Set-Cooki") then
                    cookie = string.sub(v, 0, string.find(v, ";") - 1);
                    debug("SID: " .. cookie .. "\n");
                end
            end
        else
            debug("ERROR status: " .. status .."\n")
            debug("ERROR body: " .. body .. "\n")
        end
    elseif step == "search" then
        if status == 302 or status == 200 then
            debug("success\n");
        else
            debug("ERROR status: " .. status .."\n")
            debug("ERROR body: " .. body .. "\n")
        end
        step = "logout"
    else
        if status == 200 or status == 302 then
            debug("success\n");
        else
            debug("ERROR status: " .. status .."\n")
            debug("ERROR body: " .. body .. "\n")
        end
        step = "login"
        cookie = nil
    end
    debug("Next step: " .. step .. "\n")
end

function debug(string)
    logfile:write(string)
end

function string.starts(String,Start)
   return string.sub(String,1,string.len(Start))==Start
end