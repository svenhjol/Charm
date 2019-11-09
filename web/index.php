<?php 

define('LAUNCHED', true);
require_once("vendor/autoload.php");
require_once("classes/boot.php"); 

$title = "Charm";
$category = null;
$categories = Category::getCategories();
$pathinfo = isset($_SERVER['PATH_INFO']) ? trim($_SERVER['PATH_INFO'], '/') : '';

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
        <!-- <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous"> -->
        <link rel="stylesheet" href="styles/bootstrap.min.css">
        <link rel="stylesheet" href="styles/all.min.css">
        <link rel="stylesheet" href="styles/charm.css">
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    </head>

    <body>

    <?php 
    
    if (is_null($category)) {
        require_once("home.php"); 
    } else if ($category == 404) {
        require_once("404.php");
    } else if ($category == "download") {
        require_once("download.php");
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