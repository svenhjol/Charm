<?php 

if (!defined('LAUNCHED')) die(); 

$messages = [
    "I know what I'm doing",
    "Just download it already",
    "What are you waiting for?",
    "I think this is the right link",
    "Get it from CurseForge now!",
    "Download it from CurseForge now!",
    "Get the file, I'm done waiting",
    "I want the file now",
    "Stop annoying me and just download it"
];
$downloadMessage = $messages[mt_rand(0, count($messages)-1)];

?>

<div class="container front-page">

<div class="row">
    <div class="col-12">
    <h2>Welcome to Charm!</h2>
    <p>
        Charm is a <strong>Forge Mod</strong> for <strong>Minecraft 1.14.4</strong>, inspired by <a href="">Quark</a>, that adds to the vanilla
        experience of Minecraft without drastically changing its gameplay.  Charm does not require <strong>Quark</strong> to be installed but is
        designed to play nicely with it and extend some of its features.
    </p>
    <p>
        It's a <em>modular</em> mod in that you can turn on or off any of the mod features via the config file.
    </p>
    <p>
    <span class="fa fa-question-circle"></span>&nbsp;&nbsp;Looking for Charm for <strong>Minecraft 1.12</strong>?  <a href="/old">Follow this link</a> to the old website.
    </p>
    </div>
</div>

<div class="row">
    <div class="col-12">
    <h3>Requirements</h3>
    <p>
        Charm is a <strong>Forge Mod</strong> for <strong>Minecraft 1.14.4</strong>.  You need to install the <strong>Java Edition</strong>
        of Minecraft and then the <a href="">latest version of Forge</a>.  Once you have done this, drop the <a href="">Charm file</a> in
        your <strong>mods</strong> folder and start the game.
    </p>
    <p>
        After the game starts for the first time, you can edit the <strong>charm-common.toml</strong> file (in the config folder) to change things about Charm.
        Each of Charm's features can be disabled or tweaked to fit your playstyle.
    </p>
    </div>
</div>

</div>
