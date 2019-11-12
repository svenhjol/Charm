<?php if (!defined('LAUNCHED')) die(); ?>

<nav class="navbar nav-pills nav-fill navbar-collapse justify-content-center mb-3">

<?php
$categories = Category::getCategories();
foreach ($categories as $index => $cat) {

?>
    <a href="<?php echo $cat->getId(); ?>" class="nav-link <?php if (!is_null($category) && $category->getId() == $cat->getId()) echo 'active'; ?>"><?php echo $cat->getName(); ?></a>
<?php 
} 
?>

</nav>

<?php 

if (!is_null($category)) {

?>

<div class="container mt-5 mb-5 subtitle text-center">
<h2><?php echo $category->getName(); ?></h2> 
</div>

<?php

}

?>