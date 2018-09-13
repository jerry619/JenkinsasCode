#!groovy
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import jenkins.model.*
import jenkins.security.s2m.AdminWhitelistRule
import hudson.plugins.active_directory.* 

def instance = Jenkins.getInstance()

// Disable remoting
instance.getDescriptor("jenkins.CLI").get().setEnabled(false)

// Enable Agent to master security subsystem
instance.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);

// Disable jnlp
instance.setSlaveAgentPort(-1);

//  CSRF Protection
instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Disable old Non-Encrypted protocols
HashSet<String> newProtocols = new HashSet<>(instance.getAgentProtocols());
newProtocols.removeAll(Arrays.asList(
        "JNLP3-connect", "JNLP2-connect", "JNLP-connect", "CLI-connect"
));
instance.setAgentProtocols(newProtocols);
// Set Root URL
urlConfig = JenkinsLocationConfiguration.get()
urlConfig.setUrl(System.getenv('JENKINS_URL'))
urlConfig.save()
// Set Password
String domain = 'chn.nestgroup.net'
println '--> Creating the AD realm'
String site = ''
String server = '10.15.0.4:3268'
String bindName = 'CN=gitlab,OU=OU_BAS,OU=NTP,DC=chn,DC=nestgroup,DC=net'
String bindPassword = 'nest@devops2018*'
adrealm = new ActiveDirectorySecurityRealm(domain, site, bindName, bindPassword, server)
adrealm.getDomains().each({
    it.bindName = adrealm.bindName
    it.bindPassword = adrealm.bindPassword
})
println '--> setting auth to use AD'
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)
instance.setSecurityRealm(adrealm)
instance.save()
