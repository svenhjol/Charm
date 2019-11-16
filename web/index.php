<?php 

define('LAUNCHED', true);
require_once("vendor/autoload.php");
require_once("classes/boot.php"); 

$title = "Charm";
$category = null;
$categories = Category::getCategories();
$pathinfo = isset($_SERVER['REQUEST_URI']) ? trim($_SERVER['REQUEST_URI'], '/') : '';

if (!empty($pathinfo)) {
    if (isset($categories[$pathinfo])) {
        $category = $categories[$pathinfo];
        $title = "{$title} - {$category->getName()}";
    } else if ($pathinfo == "download") {
        $category = $pathinfo;
    } else {
        $category = 404;
    }
}

?>

<!DOCTYPE html>
<html lang="en">

    <head>
        <title><?php echo $title; ?></title>
        <link rel="stylesheet" href="styles/bootstrap.min.css">
        <link rel="stylesheet" href="styles/all.min.css">
        <link rel="stylesheet" href="styles/charm.css">
        <script src="scripts/jquery-3.3.1.slim.min.js"></script>
        <script src="scripts/popper.min.js"></script>
        <script src="scripts/bootstrap.min.js"></script>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>

    <body>

    <?php 
    
    $bannerTitle = "Charm";
    $bannerSubtitle = "A vanilla+ mod for Minecraft 1.14";
    
    require_once("banner.php");
    require_once("nav.php");

    if (is_null($category)) {
        require_once("front_page.php");
    } else {
        require_once("category_page.php");
    }
    
    ?>

    <div class="container">
        <footer class="charm-footer">

        <h4>Charm for Minecraft 1.14</h4>
        <p>Developed by <a href="">Sven Hjól</a></p>
        <ul>
            <li>Playtesting and design ideas by <a href="">warlordwossman</a></li>
            <li>Art contributions by <a href="">MCVinnyq</a></li>
            <li>Some code adapted from <strong>Quark</strong> by <a href="">Vazkii</a> and <a href="">wiresegal</a></li>
        </ul>
        <p>
            Source repository: <a href="">Github</a><br>
            Please post issues on the <a href="">Github</a> tracker. Thanks!
        </p>
        </footer>
    </div>

    </body>
</html>