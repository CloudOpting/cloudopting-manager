<#list dockerDataVolumeContainers as dockerDataVolumeContainer>
${dockerDataVolumeContainer['container']}:
  image: ${dockerDataVolumeContainer['image']}
  entrypoint: ${dockerDataVolumeContainer['entrypoint']}
<#if dockerContainer['volumes']?has_content>  volumes:
<#list dockerContainer['volumes'] as volume>
    - <#if volume['container']?has_content>${volume['container']}</#if>
</#list></#if>
</#list>
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
<#if dockerContainer['environments']?has_content>  environment:
<#list dockerContainer['environments'] as environment>
    - <#if environment['envvar']?has_content>${environment['envvar']}</#if>=<#if environment['envvalue']?has_content>${environment['envvalue']}</#if>
</#list></#if>
</#list>