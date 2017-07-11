<?php
include_once('mkb10Data.php');

$getParam = iconv ( 'UTF-8' , 'windows-1251' , $_GET['query'] );

$paramKey =  preg_replace("/.*?([A-Za-z]?)[\.]?([0-9]{1,3}).*?/i","$1$2", $getParam);
$paramKey = trim($paramKey);
$paramVal =  preg_replace("/(.*?)[A-Za-z]?[\.]?[0-9]{1,3}(.*?)/i","$1$2", $getParam);
$paramVal = mb_strtolower(trim($paramVal), 'windows-1251');

echo '{';

$spanClass = 'hghl';

array_walk($mkbArr,  
	function($item, $key) use ($paramKey, $paramVal, $spanClass) 
	{ 
		$needEcho = 
				(empty($paramKey) ? false : preg_match('/.*?' . $paramKey . '.*?/i', $key)>0) || 
				(empty($paramVal) ? false : preg_match('/.*?' . $paramVal . '.*?/i', mb_strtolower($item,'windows-1251'))>0);
			
		if($needEcho) {
			$keyHL = preg_replace("/(.*?)({$paramKey})(.*?)/i","$1<span class='{$spanClass}'>$2</span>$3", $key);
			$itemHL = preg_replace("/(.*?)({$paramVal})(.*?)/i","$1<span class='{$spanClass}'>$2</span>$3", $item);
			echo "\"{$keyHL}\":\"{$itemHL}\",";
		}
	} 
);

echo "\"00\":\"00"."\"";

echo '}';





