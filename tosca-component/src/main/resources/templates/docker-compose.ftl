<#list dockerContainers as dockerContainer>
${dockerContainer['container']}:
  image: ${dockerContainer['image']}
<#if dockerContainer['volumes']?has_content>  volumes:
${dockerContainer['volumes']}</#if>
<#if dockerContainer['links']?has_content>  links:
${dockerContainer['links']}</#if>
<#if dockerContainer['exPorts']?has_content>  expose:
${dockerContainer['exPorts']}</#if>
<#if dockerContainer['ports']?has_content>  ports:
${dockerContainer['ports']}</#if>
</#list>