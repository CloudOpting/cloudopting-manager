<#list dockerContainers as dockerContainer>
${dockerContainer['container']}:
  image: ${dockerContainer['image']}
<#if dockerContainer['links']?has_content>  links:
${dockerContainer['links']}</#if>
<#if dockerContainer['exPorts']?has_content>  expose:
${dockerContainer['exPorts']}</#if>
<#if dockerContainer['ports']?has_content>  ports:
${dockerContainer['ports']}</#if>
  log_driver: "fluentd"
  log_opt:
    fluentd-address: "${dockerContainer['log_driver_address']}"
    labels: eu.cloudopting.log 
<#if dockerContainer['logtype']?has_content>  labels:
    eu.cloudopting.log: "${dockerContainer['logtype']}"</#if>
<#if dockerContainer['volumesFrom']?has_content>  volumes_from:
${dockerContainer['volumesFrom']}</#if>
<#if dockerContainer['volumes']?has_content>  volumes:
<#list dockerContainer['volumes'] as volume>
    - <#if volume['container']?has_content>${volume['container']}</#if>
</#list></#if>
</#list>