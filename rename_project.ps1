$fileNames = Get-ChildItem $args[0] -File -Recurse | Select-Object -expand fullname

# Cambio el contenido
foreach ($filename in $filenames) 
{
  (  Get-Content $fileName) -replace $args[1], $args[2] | Set-Content $fileName
}

$fileNames = Get-ChildItem $args[0] -Recurse | Select-Object -expand fullname | Sort-Object -descending

#Renombro los archivos
foreach ($filename in $filenames) 
{
  $newFileName = $filename -replace $args[1], $args[2]
  Move-Item $filename $newFileName
}
