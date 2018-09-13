println "Configuring gitlab"
def jenkins = Jenkins.getInstance()
def gitLabConfig = jenkins.getDescriptor("com.dabsquared.gitlabjenkins.connection.GitLabConnectionConfig")
def name = 'Jenkins'
def host = 'http://192.168.56.102'
def apiToken = 'Jenkins_Gitlab'
GitLabConnection connection = new GitLabConnection('name', 'url','apiToken', false ,10, 10)
boolean gitlabMissing = gitLabConfig.getConnections().findAll() {
    it.getName() == connection.name
}.empty
if(gitlabMissing) {
    println "Adding new gitlab server"
    gitLabConfig.addConnection(connection)
    gitLabConfig.save()
}
